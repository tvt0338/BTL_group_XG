package org.example;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bomber {
    private double x, y;
    private ImageView imageView;

    public Bomber(double x, double y) {
        this.x = x;
        this.y = y;

        Image image = new Image(Bomber.class.getResource("/bomber.jpg").toExternalForm());
        this.imageView = new ImageView(image);
        this.imageView.setFitWidth(40);
        this.imageView.setFitHeight(40);
        this.imageView.setX(x);
        this.imageView.setY(y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
        imageView.setX(x);
    }

    public void setY(double y) {
        this.y = y;
        imageView.setY(y);
    }

    public ImageView getImageView() {
        return imageView;
    }
}
