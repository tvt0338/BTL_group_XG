package enemy;

import bomberman.Player;
import bomb.Bomb;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import java.util.List;
import java.util.Random;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.LinkedList;

public class Void extends Monster {
    private static Image[][] sprites; // [hướng][frame]
    private static final int INVISIBLE_DURATION = 420; // số frame tàng hình
    private static final int VISIBLE_DURATION = 180; // số frame hiện hình
    private int direction = 1; // 0: up, 1: down, 2: left, 3: right
    private int frame = 0;
    private int frameCounter = 0;
    private int visibleCounter = 0;
    private boolean isVisible = true;
    private int[][] mapData;
    private int cellSize;
    private List<Bomb> bombs;
    private Player player;

    private Random random = new Random();
    private double px, py; // vị trí thực tế dạng double
    private double vx = 0, vy = 0;
    private final double SPEED = 1.4; // Slightly faster than Oneal

    public Void(int x, int y, int moveThreshold, int cellSize, Player player) {
        super(x, y, 1, 1, cellSize);
        this.cellSize = cellSize;
        this.player = player;
        this.px = x * cellSize;
        this.py = y * cellSize;
        randomizeDirection();
    }

    public static void loadSprites() {
        sprites = new Image[4][3];
        sprites[0][0] = new Image(Void.class.getResourceAsStream("/enemy/images/void_up1.png"));
        sprites[0][1] = new Image(Void.class.getResourceAsStream("/enemy/images/void_up2.png"));
        sprites[0][2] = new Image(Void.class.getResourceAsStream("/enemy/images/void_up1.png"));
        sprites[1][0] = new Image(Void.class.getResourceAsStream("/enemy/images/void_down1.png"));
        sprites[1][1] = new Image(Void.class.getResourceAsStream("/enemy/images/void_up2.png"));
        sprites[1][2] = new Image(Void.class.getResourceAsStream("/enemy/images/void_up1.png"));
        sprites[2][0] = new Image(Void.class.getResourceAsStream("/enemy/images/void_left1.png"));
        sprites[2][1] = new Image(Void.class.getResourceAsStream("/enemy/images/void_left2.png"));
        sprites[2][2] = new Image(Void.class.getResourceAsStream("/enemy/images/void_left3.png"));
        sprites[3][0] = new Image(Void.class.getResourceAsStream("/enemy/images/void_right1.png"));
        sprites[3][1] = new Image(Void.class.getResourceAsStream("/enemy/images/void_right2.png"));
        sprites[3][2] = new Image(Void.class.getResourceAsStream("/enemy/images/void_right3.png"));
    }

    public void setMapData(int[][] mapData, int cellSize) {
        this.mapData = mapData;
        this.cellSize = cellSize;
    }

    public void setBombs(List<Bomb> bombs) {
        this.bombs = bombs;
    }

    @Override
    public void update() {
        if (!isAlive) return;

        // Update visibility state
        visibleCounter++;
        if (isVisible && visibleCounter >= VISIBLE_DURATION) {
            isVisible = false;
            visibleCounter = 0;
        } else if (!isVisible && visibleCounter >= INVISIBLE_DURATION) {
            isVisible = true;
            visibleCounter = 0;
        }

        // Chỉ đổi hướng khi đang ở tâm ô lưới
        boolean atGridCenter = (Math.abs((px + cellSize / 2) % cellSize - cellSize / 2) < 0.1) &&
                (Math.abs((py + cellSize / 2) % cellSize - cellSize / 2) < 0.1);

        if (atGridCenter) {
            setVelocityToChasePlayer();
        }

        double nextPx = px + vx;
        double nextPy = py + vy;
        if (canMoveToPixel(nextPx, nextPy)) {
            px = nextPx;
            py = nextPy;
        } else {
            // Snap về lưới gần nhất nếu bị lệch
            px = Math.round(px / cellSize) * cellSize;
            py = Math.round(py / cellSize) * cellSize;
            setVelocityToChasePlayer();
        }

        // Cập nhật lại x, y theo pixel
        x = (int) Math.round(px / cellSize);
        y = (int) Math.round(py / cellSize);

        // Kiểm tra va chạm vùng nổ bomb
        if (bombs != null) {
            for (Bomb bomb : bombs) {
                if (bomb.isInExplosionArea(px + size / 2, py + size / 2)) {
                    takeDamage(1);
                    break;
                }
            }
        }

        // Đổi frame để tạo hiệu ứng động
        frameCounter++;
        if (frameCounter >= 8) { // Slightly faster animation than Oneal
            frame = (frame + 1) % 3;
            frameCounter = 0;
        }
    }

    private void setVelocityToChasePlayer() {
        int startX = (int) Math.round(px / cellSize);
        int startY = (int) Math.round(py / cellSize);
        int goalX = (int) Math.round(player.getX() / cellSize);
        int goalY = (int) Math.round(player.getY() / cellSize);
        int manhattan = Math.abs(goalX - startX) + Math.abs(goalY - startY);

        if (manhattan <= 1) {
            // Nếu gần player, di chuyển thẳng đến player
            double dx = player.getX() - px;
            double dy = player.getY() - py;
            if (Math.abs(dx) > Math.abs(dy)) {
                vx = (dx > 0) ? SPEED : -SPEED;
                vy = 0;
                direction = (dx > 0) ? 3 : 2;
            } else {
                vy = (dy > 0) ? SPEED : -SPEED;
                vx = 0;
                direction = (dy > 0) ? 1 : 0;
            }
            return;
        }

        // A* tìm đường tối ưu đến player
        int[][] dirs = {{0,-1},{0,1},{-1,0},{1,0}}; // up, down, left, right
        int[][] prev = new int[mapData.length * mapData[0].length][2];
        boolean[][] visited = new boolean[mapData.length][mapData[0].length];

        class Node {
            int x, y, g, f;
            Node(int x, int y, int g, int f) { this.x = x; this.y = y; this.g = g; this.f = f; }
        }

        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        open.add(new Node(startX, startY, 0, manhattan));
        visited[startY][startX] = true;
        boolean found = false;

        while (!open.isEmpty()) {
            Node cur = open.poll();
            if (cur.x == goalX && cur.y == goalY) {
                found = true;
                break;
            }

            for (int d = 0; d < 4; d++) {
                int nx = cur.x + dirs[d][0];
                int ny = cur.y + dirs[d][1];
                if (nx < 0 || ny < 0 || ny >= mapData.length || nx >= mapData[0].length) continue;
                if (visited[ny][nx]) continue;
                int tile = mapData[ny][nx];
                if (tile == 1 || tile == 3) continue;
                visited[ny][nx] = true;
                prev[ny * mapData[0].length + nx] = new int[]{cur.x, cur.y};
                int g = cur.g + 1;
                int h = Math.abs(goalX - nx) + Math.abs(goalY - ny);
                open.add(new Node(nx, ny, g, g + h));
            }
        }

        if (found) {
            int cx = goalX, cy = goalY;
            int px1 = startX, py1 = startY;
            LinkedList<int[]> path = new LinkedList<>();
            while (!(cx == px1 && cy == py1)) {
                path.addFirst(new int[]{cx, cy});
                int[] p = prev[cy * mapData[0].length + cx];
                cx = p[0];
                cy = p[1];
            }

            if (!path.isEmpty()) {
                int[] next = path.getFirst();
                int dx = next[0] - startX;
                int dy = next[1] - startY;
                if (dx == 1) { vx = SPEED; vy = 0; direction = 3; }
                else if (dx == -1) { vx = -SPEED; vy = 0; direction = 2; }
                else if (dy == 1) { vy = SPEED; vx = 0; direction = 1; }
                else if (dy == -1) { vy = -SPEED; vx = 0; direction = 0; }
            }
        } else {
            // Nếu không tìm được đường, random hướng
            randomizeDirection();
        }
    }

    private void randomizeDirection() {
        int dir = random.nextInt(4);
        switch (dir) {
            case 0: vx = 0; vy = -SPEED; direction = 0; break; // up
            case 1: vx = 0; vy = SPEED; direction = 1; break;  // down
            case 2: vx = -SPEED; vy = 0; direction = 2; break; // left
            case 3: vx = SPEED; vy = 0; direction = 3; break;  // right
        }
    }

    private boolean canMoveToPixel(double nextPx, double nextPy) {
        // 4 góc của Void
        double left = nextPx;
        double right = nextPx + cellSize - 1;
        double top = nextPy;
        double bottom = nextPy + cellSize - 1;

        int leftCol = (int) (left / cellSize);
        int rightCol = (int) (right / cellSize);
        int topRow = (int) (top / cellSize);
        int bottomRow = (int) (bottom / cellSize);

        int[] tiles = {
                mapData[topRow][leftCol],
                mapData[topRow][rightCol],
                mapData[bottomRow][leftCol],
                mapData[bottomRow][rightCol]
        };
        for (int tile : tiles) {
            if (tile == 1 || tile == 3) return false;
        }
        if (bombs != null) {
            for (Bomb bomb : bombs) {
                int bombCol = (int) Math.floor(bomb.getX() / cellSize);
                int bombRow = (int) Math.floor(bomb.getY() / cellSize);
                if ((bombCol == leftCol && bombRow == topRow) ||
                        (bombCol == rightCol && bombRow == topRow) ||
                        (bombCol == leftCol && bombRow == bottomRow) ||
                        (bombCol == rightCol && bombRow == bottomRow)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean dealDamage(Player player) {
        // Nếu trùng vị trí thì gây damage cho player
        int pxPlayer = (int) Math.floor(player.getX() / cellSize);
        int pyPlayer = (int) Math.floor(player.getY() / cellSize);
        if (x == pxPlayer && y == pyPlayer && player.getHp() > 0) {
            player.takeDamage();
            System.out.println("Void deals 1 dmg to Bomberman.");
            return true;
        }
        return false;
    }

    @Override
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            isAlive = false;
            System.out.println("Void destroyed!");
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isAlive) return;
        if (sprites == null) return;
        if (!isVisible && !player.hasGodEye()) return;

        // Đảm bảo frame nằm trong 0..2
        int frameIdx = frame % 3;
        Image currentSprite = sprites[direction][frameIdx];

        // Vẽ với kích thước phóng đại
        double scale = 1.5;
        double drawSize = cellSize * scale;
        double offset = (drawSize - cellSize) / 2; // Để căn giữa
        gc.drawImage(currentSprite,
                px - offset,
                py - offset,
                drawSize,
                drawSize
        );
    }
}