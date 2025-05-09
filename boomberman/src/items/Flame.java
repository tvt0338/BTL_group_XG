package items;

import bomberman.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Flame extends Item {
    private static Image sprite;
    private double x, y;
    private final int size = 40; // Kích thước item

    public Flame(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static void loadSprite() {
        try {
            sprite = new Image(Flame.class.getResourceAsStream("/items/images/flame.gif"));
        } catch (Exception e) {
            System.out.println("Error loading flame item sprite: " + e.getMessage());
        }
    }

    @Override
    public void applyEffect(Player player) {
        player.increaseBombRange(); // tăng phạm vi nổ vĩnh viễn
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