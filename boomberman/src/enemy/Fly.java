package enemy;

import bomberman.Player;
import bomb.Bomb;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import java.util.List;
import java.util.Random;

public class Fly extends Monster {
    private static Image[] gifFrames; // Mảng chứa các frame của GIF
    private int currentFrame = 0;
    private int frameCounter = 0;
    private int[][] mapData;
    private int cellSize;
    private List<Bomb> bombs;
    private Player player;
    private double px, py; // Vị trí thực tế để di chuyển mượt
    private double speed = 1.0; // Tốc độ bay nhanh hơn các enemy khác

    public Fly(int x, int y,double speed , int cellSize, Player player) {
        super(x, y, 1, 1, cellSize); // 1hp
        this.cellSize = cellSize;
        this.player = player;
        this.px = x * cellSize;
        this.py = y * cellSize;
        this.speed = speed;
    }

    public static void loadSprites() {
        // Load 4 frame cho animation bay (có thể điều chỉnh số lượng frame)
        gifFrames = new Image[4];
        gifFrames[0] = new Image(Fly.class.getResourceAsStream("/enemy/images/fly1.png"));
        gifFrames[1] = new Image(Fly.class.getResourceAsStream("/enemy/images/fly2.png"));
        gifFrames[2] = new Image(Fly.class.getResourceAsStream("/enemy/images/fly3.png"));
        gifFrames[3] = new Image(Fly.class.getResourceAsStream("/enemy/images/fly4.png"));
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


        // Bay thẳng đến player mà không cần thuật toán tìm đường
        double dx = player.getX() - px;
        double dy = player.getY() - py;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Chuẩn hóa vector hướng
        if (distance > 0) {
            dx /= distance;
            dy /= distance;
        }

        // Cập nhật vị trí
        px += dx * speed;
        py += dy * speed;

        // Cập nhật vị trí ô lưới
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

        // Cập nhật animation
        frameCounter++;
        if (frameCounter >= 8) { // Tốc độ animation
            currentFrame = (currentFrame + 1) % gifFrames.length;
            frameCounter = 0;
        }
    }


    @Override
    public boolean dealDamage(Player player) {
        // Kiểm tra va chạm với player bằng khoảng cách
        double distance = Math.sqrt(
                Math.pow(player.getX() - (px + size/2), 2) +
                        Math.pow(player.getY() - (py + size/2), 2));

        if (distance < cellSize/2 && player.getHp() > 0) {
            player.takeDamage();
            System.out.println("Fly deals 1 dmg to Bomberman.");
            return true;
        }
        return false;
    }

    @Override
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            isAlive = false;
            System.out.println("Fly destroyed!");
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isAlive || gifFrames == null) return;

        // Vẽ frame hiện tại của animation
        Image currentImage = gifFrames[currentFrame];
        gc.drawImage(currentImage, px, py, cellSize, cellSize);

        //  Vẽ hitbox để debug
        // gc.setStroke(Color.RED);
        // gc.strokeRect(px, py, cellSize, cellSize);
    }
}