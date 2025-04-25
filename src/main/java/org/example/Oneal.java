package org.example;

import javafx.scene.image.Image;

public class Oneal extends Enemy {

    private Bomber bomber;

    public Oneal(double x, double y, Bomber bomber) {
        super(x, y, new Image(Oneal.class.getResource("/oneal.jpg").toExternalForm()));
        this.bomber = bomber;
    }

    @Override
    public void update() {
        if (!alive) return;
        if (isBomberNear()) {
            chaseBomber();
        } else {
            moveRandomly();
        }
    }

    private boolean isBomberNear() {
        double dx = Math.abs(bomber.getX() - this.x);
        double dy = Math.abs(bomber.getY() - this.y);
        return dx + dy < 100;
    }

    private void chaseBomber() {
        if (bomber.getX() > x) x += speed;
        else if (bomber.getX() < x) x -= speed;

        if (bomber.getY() > y) y += speed;
        else if (bomber.getY() < y) y -= speed;
    }

    private void moveRandomly() {
        if (random.nextInt(20) == 0) {
            int dir = random.nextInt(4);
            switch (dir) {
                case 0 -> y -= speed;
                case 1 -> y += speed;
                case 2 -> x -= speed;
                case 3 -> x += speed;
            }
        }
    }
}
