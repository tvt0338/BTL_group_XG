package bomberman;

import bomb.Bomb;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Player {
    private double x, y;
    private final double radius = 20;
    private double speed = 3; // số pixel mỗi frame
    private double originalSpeed = 3; // lưu tốc độ ban đầu
    private boolean isSpeedBoosted = false; // trạng thái tăng tốc
    private int hp = 3; // HP hiện tại
    private int maxHp = 3; // HP tối đa có thể tăng lên
    private int[][] mapData; // Thêm map data để kiểm tra va chạm
    private int cellSize; // Kích thước mỗi ô trong map
    private int bombRange = 1; // phạm vi nổ mặc định
    private int originalBombRange = 1; // lưu phạm vi nổ ban đầu
    private boolean hasGodEye = false; // trạng thái God Eye
    private boolean isBombRangeBoosted = false; // trạng thái tăng phạm vi nổ

    private List<Bomb> bombs = new ArrayList<>();
    private static final int MAX_BOMBS = 2; // Giới hạn số lượng bomb có thể đặt cùng lúc

    private static Image[][] sprites;
    private int direction = 1; // xuống
    private int frame = 0;
    private int frameCounter = 0;
    private static final int ANIMATION_SPEED = 8; // Tốc độ animation

    private CollisionHandler collisionHandler;

    private boolean wasInExplosionLastFrame = false;

    private long speedEffectEndTime = 0; // thời điểm kết thúc hiệu ứng tốc độ
    private long godEyeEffectEndTime = 0; // thời điểm kết thúc hiệu ứng God Eye

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static void loadSprites(String basePath) throws FileNotFoundException {
        sprites = new Image[4][2];
        sprites[0][0] = new Image(new FileInputStream(basePath + "up_1.png"));
        sprites[0][1] = new Image(new FileInputStream(basePath + "up_2.png"));
        sprites[1][0] = new Image(new FileInputStream(basePath + "down_1.png"));
        sprites[1][1] = new Image(new FileInputStream(basePath + "down_2.png"));
        sprites[2][0] = new Image(new FileInputStream(basePath + "left_1.png"));
        sprites[2][1] = new Image(new FileInputStream(basePath + "left_2.png"));
        sprites[3][0] = new Image(new FileInputStream(basePath + "right_1.png"));
        sprites[3][1] = new Image(new FileInputStream(basePath + "right_2.png"));
    }

    public void setMapData(int[][] mapData, int cellSize) {
        this.mapData = mapData;
        this.cellSize = cellSize;
        collisionHandler = new CollisionHandler(mapData, cellSize, radius);
    }

    public void update(Set<KeyCode> keys) {
        if (hp <= 0) return;

        // Kiểm tra và reset tốc độ nếu hiệu ứng đã hết
        if (speedEffectEndTime > 0 && System.currentTimeMillis() > speedEffectEndTime) {
            System.out.println("Speed effect expired. Resetting speed...");
            resetSpeed();
            speedEffectEndTime = 0;
        }

        // Kiểm tra và reset God Eye nếu hiệu ứng đã hết
        if (godEyeEffectEndTime > 0 && System.currentTimeMillis() > godEyeEffectEndTime) {
            deactivateGodEye();
            godEyeEffectEndTime = 0;
        }

        boolean moving = false;
        double currentSpeed = isSpeedBoosted ? originalSpeed * 2 : originalSpeed;

        // Xử lý di chuyển theo từng hướng riêng biệt
        if (keys.contains(KeyCode.W)) {
            double newY = y - currentSpeed;
            if (collisionHandler.canMoveTo(x, newY, "up")) {
                y = newY;
                direction = 0;
                moving = true;
            }
        }
        if (keys.contains(KeyCode.S)) {
            double newY = y + currentSpeed;
            if (collisionHandler.canMoveTo(x, newY, "down")) {
                y = newY;
                direction = 1;
                moving = true;
            }
        }
        if (keys.contains(KeyCode.A)) {
            double newX = x - currentSpeed;
            if (collisionHandler.canMoveTo(newX, y, "left")) {
                x = newX;
                direction = 2;
                moving = true;
            }
        }
        if (keys.contains(KeyCode.D)) {
            double newX = x + currentSpeed;
            if (collisionHandler.canMoveTo(newX, y, "right")) {
                x = newX;
                direction = 3;
                moving = true;
            }
        }

        // Cập nhật animation
        if (moving) {
            frameCounter++;
            if (frameCounter >= ANIMATION_SPEED) {
                frame = (frame + 1) % 2;
                frameCounter = 0;
            }
        } else {
            frame = 0; // Reset về frame đầu tiên khi không di chuyển
            frameCounter = 0;
        }

        // Xử lý đặt bomb
        if (keys.contains(KeyCode.SPACE)) {
            System.out.println("Space pressed. Current bombs: " + bombs.size());
            if (bombs.size() < MAX_BOMBS) {
                placeBomb();
            }
        }

        // Kiểm tra va chạm với vùng nổ của bomb
        boolean inExplosion = false;
        for (Bomb bomb : bombs) {
            if (bomb.isInExplosionArea(x, y)) {
                inExplosion = true;
                break;
            }
        }
        if (inExplosion && !wasInExplosionLastFrame) {
            takeDamage();
        }
        wasInExplosionLastFrame = inExplosion;

        // Xóa các bomb đã hết thời gian
        bombs.removeIf(Bomb::isExpired);
    }

    private void placeBomb() {
        Bomb newBomb = new Bomb(x, y, this);
        bombs.add(newBomb);
        System.out.println("Placed new bomb. Total bombs now: " + bombs.size());
    }

    public void render(GraphicsContext gc) {
        if (hp <= 0) return; // Không vẽ nếu đã chết

        // Vẽ các quả bom
        for (Bomb bomb : bombs) {
            bomb.render(gc);
        }
        // Vẽ ảnh player thay vì hình tròn
        Image currentSprite = sprites[direction][frame];
        gc.drawImage(currentSprite, x - radius, y - radius, radius * 2, radius * 2);

        // Vẽ thanh HP
        gc.setFill(Color.RED);
        gc.fillRect(x - radius, y - radius - 10, radius * 2, 5);
        gc.setFill(Color.GREEN);
        gc.fillRect(x - radius, y - radius - 10, radius * 2 * ((double)hp / maxHp), 5);
    }

    // Getter cho x và y
    public double getX() { return x; }
    public double getY() { return y; }

    // Getter và setter cho HP
    public int getHp() { return hp; }
    public void setHp(int hp) {
        this.hp = hp;
        if (hp > maxHp) {
            maxHp = hp; // Cập nhật maxHp nếu HP hiện tại lớn hơn
        }
    }

    public void takeDamage() {
        hp--;
        if (hp <= 0) {
            // Xử lý khi player chết
            System.out.println("Player died!");
        }
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public double getSize() {
        return radius * 2; // Trả về đường kính của player
    }

    public void increaseSpeed() {
        System.out.println("Increasing speed from " + speed + " to " + (originalSpeed * 2));
        isSpeedBoosted = true;
        speed = originalSpeed * 2;
    }

    public void resetSpeed() {
        System.out.println("Resetting speed from " + speed + " to " + originalSpeed);
        isSpeedBoosted = false;
        speed = originalSpeed;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        System.out.println("Setting speed from " + this.speed + " to " + speed);
        this.originalSpeed = speed;
        this.speed = isSpeedBoosted ? speed * 2 : speed;
    }

    public void increaseBombRange() {
        this.bombRange++; // tăng phạm vi nổ thêm 1
        this.originalBombRange++; // cập nhật cả phạm vi nổ gốc
    }

    public void resetBombRange() {
        this.bombRange = originalBombRange;
    }

    public int getBombRange() {
        return bombRange;
    }

    public void activateGodEye() {
        this.hasGodEye = true;
    }

    public void deactivateGodEye() {
        this.hasGodEye = false;
    }

    public boolean hasGodEye() {
        return hasGodEye;
    }

    public void setSpeedEffectEndTime(long endTime) {
        this.speedEffectEndTime = endTime;
    }

    public void setGodEyeEffectEndTime(long endTime) {
        this.godEyeEffectEndTime = endTime;
    }

    // Thêm các getter/setter khác nếu cần
}
