package org.example;

import javafx.scene.image.Image;

public class Fly extends Enemy {

    private Bomber bomber;

    public Fly(double x, double y, Bomber bomber) {
        super(x, y, new Image(Fly.class.getResource("/fly.jpg").toExternalForm()));
        this.bomber = bomber;
        this.speed = 1.5; // Có thể nhanh hơn
    }

    @Override
    public void update() {
        if (!alive) return;
        chaseBomber();
    }

    private void chaseBomber() {
        double dx = bomber.getX() - this.x;
        double dy = bomber.getY() - this.y;

        double length = Math.sqrt(dx * dx + dy * dy);

        if (length != 0) {
            x += (dx / length) * speed;
            y += (dy / length) * speed;
        }
    }
}
