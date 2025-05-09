package items;

import bomberman.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Health extends Item {
    private static Image sprite;
    private double x, y;
    private final int size = 40; // Kích thước item

    public Health(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static void loadSprite() {
        try {
            sprite = new Image(Health.class.getResourceAsStream("/items/images/tym.gif"));
        } catch (Exception e) {
            System.out.println("Error loading health item sprite: " + e.getMessage());
        }
    }

    @Override
    public void applyEffect(Player player) {
        // Tăng HP vĩnh viễn thêm 1
        player.setHp(player.getHp() + 1);
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