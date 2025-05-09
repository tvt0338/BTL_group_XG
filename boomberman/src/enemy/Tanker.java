package enemy;

import bomberman.Player;
import bomb.Bomb;
import java.util.List;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayDeque;

public class Tanker extends Monster {
    private static Image[][] sprites; // [hướng][frame]
    private int direction = 1; // 0: up, 1: down, 2: left, 3: right
    private int frame = 0;
    private int frameCounter = 0;
    private int[][] mapData;
    private int cellSize;
    private List<Bomb> bombs;
    private Player player;
    private Random random = new Random();
    private double px, py; // vị trí thực tế dạng double
    private double vx = 0, vy = 0;
    private final double SPEED = 0.8; // Tốc độ chậm hơn Oneal

    public Tanker(int x, int y, int moveThreshold, int size, Player player) {
        super(x, y, 3, 3, size); // 3hp
        this.player = player;
        this.px = x * size;
        this.py = y * size;
        randomizeDirection();
    }

    public static void loadSprites() {
        sprites = new Image[4][4];
        sprites[0][0] = new Image(Tanker.class.getResourceAsStream("/enemy/images/tanker_left1.png"));
        sprites[0][1] = new Image(Tanker.class.getResourceAsStream("/enemy/images/tanker_left2.png"));
        sprites[0][2] = new Image(Tanker.class.getResourceAsStream("/enemy/images/tanker_left3.png"));
        sprites[0][3] = new Image(Tanker.class.getResourceAsStream("/enemy/images/tanker_left4.png"));
        sprites[1][0] = new Image(Tanker.class.getResourceAsStream("/enemy/images/tanker_right1.png"));
        sprites[1][1] = new Image(Tanker.class.getResourceAsStream("/enemy/images/tanker_right2.png"));
        sprites[1][2] = new Image(Tanker.class.getResourceAsStream("/enemy/images/tanker_right3.png"));
        sprites[1][3] = new Image(Tanker.class.getResourceAsStream("/enemy/images/tanker_right4.png"));
        sprites[2][0] = new Image(Tanker.class.getResourceAsStream("/enemy/images/tanker_left1.png"));
        sprites[2][1] = new Image(Tanker.class.getResourceAsStream("/enemy/images/tanker_left2.png"));
        sprites[2][2] = new Image(Tanker.class.getResourceAsStream("/enemy/images/tanker_left3.png"));
        sprites[2][3] = new Image(Tanker.class.getResourceAsStream("/enemy/images/tanker_left4.png"));
        sprites[3][0] = new Image(Tanker.class.getResourceAsStream("/enemy/images/tanker_right1.png"));
        sprites[3][1] = new Image(Tanker.class.getResourceAsStream("/enemy/images/tanker_right2.png"));
        sprites[3][2] = new Image(Tanker.class.getResourceAsStream("/enemy/images/tanker_right3.png"));
        sprites[3][3] = new Image(Tanker.class.getResourceAsStream("/enemy/images/tanker_right4.png"));
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

        // Đổi frame để tạo hiệu ứng động (chậm hơn Oneal)
        frameCounter++;
        if (frameCounter >= 15) {
            frame = (frame + 1) % 4;
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

        // BFS tìm đường đến player
        int[][] dirs = {{0,-1},{0,1},{-1,0},{1,0}}; // up, down, left, right
        int[][] prev = new int[mapData.length * mapData[0].length][2];
        boolean[][] visited = new boolean[mapData.length][mapData[0].length];

        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{startX, startY});
        visited[startY][startX] = true;
        boolean found = false;

        while (!queue.isEmpty() && !found) {
            int[] current = queue.poll();

            for (int d = 0; d < 4; d++) {
                int nx = current[0] + dirs[d][0];
                int ny = current[1] + dirs[d][1];

                if (nx < 0 || ny < 0 || ny >= mapData.length || nx >= mapData[0].length) continue;
                if (visited[ny][nx]) continue;

                int tile = mapData[ny][nx];
                if (tile == 1 || tile == 3) continue;

                visited[ny][nx] = true;
                prev[ny * mapData[0].length + nx] = new int[]{current[0], current[1]};

                if (nx == goalX && ny == goalY) {
                    found = true;
                    break;
                }

                queue.add(new int[]{nx, ny});
            }
        }

        if (found) {
            // Truy vết đường đi ngược từ goal về start
            LinkedList<int[]> path = new LinkedList<>();
            int cx = goalX, cy = goalY;

            while (!(cx == startX && cy == startY)) {
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
        // 4 góc của Tanker
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
        int pxPlayer = (int) Math.floor(player.getX() / cellSize);
        int pyPlayer = (int) Math.floor(player.getY() / cellSize);
        if (x == pxPlayer && y == pyPlayer && player.getHp() > 0) {
            player.takeDamage();
            System.out.println("Tanker deals 1 dmg to Bomberman.");
            return true;
        }
        return false;
    }

    @Override
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            isAlive = false;
            System.out.println("Tanker destroyed!");
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isAlive) return;
        if (sprites == null) return;
        Image currentSprite = sprites[direction][frame % 4];
        gc.drawImage(currentSprite, px, py, cellSize, cellSize);
    }
}