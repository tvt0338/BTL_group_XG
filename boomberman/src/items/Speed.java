package items;

import bomberman.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Speed extends Item {
    private static Image sprite;
    private double x, y;
    private final int size = 40; // Kích thước item
    private static final long EFFECT_DURATION = 7000; // 7 giây

    public Speed(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static void loadSprite() {
        try {
            sprite = new Image(Speed.class.getResourceAsStream("/items/images/speed.gif"));
        } catch (Exception e) {
            System.out.println("Error loading speed item sprite: " + e.getMessage());
        }
    }

    @Override
    public void applyEffect(Player player) {
        player.increaseSpeed(); // tăng tốc gấp đôi
        player.setSpeedEffectEndTime(System.currentTimeMillis() + EFFECT_DURATION);
    }

    public void render(GraphicsContext gc) {
        if (sprite != null) {
            gc.drawImage(sprite, x - size/2, y - size/2, size, size);
        }
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public int getSize() { return size; }
}