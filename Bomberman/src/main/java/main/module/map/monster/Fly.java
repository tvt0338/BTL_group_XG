package main.module.map.monster;

import main.module.map.GameCanvas;
import main.module.map.entities.Entity;

import java.util.Random;

public class Fly extends Entity {
    public Fly(GameCanvas gc) {
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
        up1 = setup("/Images/monster/bat_down_1");
        up2 = setup("/Images/monster/bat_down_2");
        down1 = setup("/Images/monster/bat_down_1");
        down2 = setup("/Images/monster/bat_down_2");
        left1 = setup("/Images/monster/bat_down_1");
        left2 = setup("/Images/monster/bat_down_2");
        right1 = setup("/Images/monster/bat_down_1");
        right2 = setup("/Images/monster/bat_down_2");

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
