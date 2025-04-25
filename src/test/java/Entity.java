package bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.geometry.Rectangle2D;

public abstract class Entity {
    protected double x, y;
    protected Image image;
    protected boolean alive = true;

    public Entity(double x, double y, Image image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }

    public abstract void update();

    public void render(GraphicsContext gc) {
        gc.drawImage(image, x, y);
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(x, y, image.getWidth(), image.getHeight());
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean value) {
        this.alive = value;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}
