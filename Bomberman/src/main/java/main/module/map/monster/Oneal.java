package main.module.map.monster;

import main.module.map.GameCanvas;
import main.module.map.entities.Entity;

import java.util.Random;

public class Oneal extends Entity {
    public Oneal(GameCanvas gc) {
        super(gc);

        type = 1;
        name = "balloon";
        speed = 1;
        maxLife = 1;
        life = maxLife;

        solidArea.x = 4;
        solidArea.y = 8;
        solidArea.width = 24;
        solidArea.height = 24;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        up1 = setup("/Images/monster/Oneal_left");
        up2 = setup("/Images/monster/Oneal_right");
        down1 = setup("/Images/monster/Oneal_left");
        down2 = setup("/Images/monster/Oneal_right");
        left1 = setup("/Images/monster/Oneal_left");
        left2 = setup("/Images/monster/Oneal_left");
        right1 = setup("/Images/monster/Oneal_right");
        right2 = setup("/Images/monster/Oneal_right");

    }

    public void setAction() {
        actionLockCounter ++;
        if (actionLockCounter == 120) {

            String[] directions = {"up", "down", "left", "right"};
            int i = new Random().nextInt(directions.length);
            direction = directions[i];

            actionLockCounter = 0;

        }
    }
}
