package org.example;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Entity {
    protected double x, y;
    protected double speed;
    protected boolean alive = true;
    protected Image image;
    protected ImageView imageView;
    protected int width, height;

    public Entity(double x, double y, Image image) {
        this.x = x;
        this.y = y;
        this.image = image;

        this.imageView = new ImageView(image);
        this.imageView.setFitWidth(40);
        this.imageView.setFitHeight(40);
        this.imageView.setX(x);
        this.imageView.setY(y);

        this.width = (int) image.getWidth();
        this.height = (int) image.getHeight();
    }

    public abstract void update();

    public ImageView getImageView() {
        return imageView;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        alive = false;
        imageView.setVisible(false);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
