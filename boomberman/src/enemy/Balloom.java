package enemy;

import bomberman.Player;
import bomb.Bomb;
import java.util.List;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;

public class Balloom extends Monster {
    private final Random random = new Random();
    private int[][] mapData;
    private int cellSize;
    private List<Bomb> bombs;
    private int moveCounter = 0;
    private int moveThreshold; // Số frame chờ giữa mỗi lần di chuyển
    private static Image[][] sprites; // [hướng][frame]
    private int direction = 1; // 0: up, 1: down, 2: left, 3: right
    private int frame = 0;
    private int frameCounter = 0;
    private double px, py; // vị trí thực tế dạng double
    private double vx = 0, vy = 0;
    private final double SPEED = 1.2;

    public Balloom(int x, int y, int moveThreshold, int size) {
        super(x, y, 1, 1, size);
        this.moveThreshold = moveThreshold;
        this.px = x * size;
        this.py = y * size;
        randomizeDirection();
    }

    public static void loadSprites() {
        sprites = new Image[4][2];
        sprites[0][0] = new Image(Balloom.class.getResourceAsStream("/enemy/images/balloom_up1.png"));
        sprites[0][1] = new Image(Balloom.class.getResourceAsStream("/enemy/images/balloom_up2.png"));
        sprites[1][0] = new Image(Balloom.class.getResourceAsStream("/enemy/images/balloom_down.png"));
        sprites[1][1] = new Image(Balloom.class.getResourceAsStream("/enemy/images/balloom_mid.png"));
        sprites[2][0] = new Image(Balloom.class.getResourceAsStream("/enemy/images/balloom_left1.png"));
        sprites[2][1] = new Image(Balloom.class.getResourceAsStream("/enemy/images/balloom_left2.png"));
        sprites[3][0] = new Image(Balloom.class.getResourceAsStream("/enemy/images/balloom_right1.png"));
        sprites[3][1] = new Image(Balloom.class.getResourceAsStream("/enemy/images/balloom_right2.png"));
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
        // Di chuyển liên tục
        double nextPx = px + vx;
        double nextPy = py + vy;
        if (canMoveToPixel(nextPx, nextPy)) {
            px = nextPx;
            py = nextPy;
        } else {
            randomizeDirection();
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
        if (frameCounter >= 10) {
            frame = (frame + 1) % 2;
            frameCounter = 0;
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
        // 4 góc của Balloom
        double left = nextPx;
        double right = nextPx + cellSize - 1;
        double top = nextPy;
        double bottom = nextPy + cellSize - 1;

        int leftCol = (int) (left / cellSize);
        int rightCol = (int) (right / cellSize);
        int topRow = (int) (top / cellSize);
        int bottomRow = (int) (bottom / cellSize);

        // Kiểm tra 4 góc đều không nằm trong tường
        int[] tiles = {
                mapData[topRow][leftCol],
                mapData[topRow][rightCol],
                mapData[bottomRow][leftCol],
                mapData[bottomRow][rightCol]
        };
        for (int tile : tiles) {
            if (tile == 1 || tile == 3) return false;
        }
        // Kiểm tra bomb như cũ
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
        int px = (int) Math.floor(player.getX() / cellSize);
        int py = (int) Math.floor(player.getY() / cellSize);
        if (x == px && y == py && player.getHp() > 0) {
            player.takeDamage();
            System.out.println("Balloom deals 1 dmg to Bomberman.");
            return true;
        }
        return false;
    }

    @Override
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            isAlive = false;
            System.out.println("Balloom destroyed!");
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isAlive) return;
        if (sprites == null) return;
        Image currentSprite = sprites[direction][frame];
        gc.drawImage(currentSprite, px, py, cellSize, cellSize);
    }
}