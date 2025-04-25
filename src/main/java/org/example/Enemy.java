package org.example;

import javafx.scene.image.Image;
import java.util.Random;

public abstract class Enemy extends Entity {
    protected int hp = 1;
    protected String direction = "down";
    protected Random random = new Random();

    public Enemy(double x, double y, Image image) {
        super(x, y, image);
        this.speed = 1.0;
    }

    public void takeDamage(int dmg) {
        hp -= dmg;
        if (hp <= 0) {
            kill();
        }
    }


}
