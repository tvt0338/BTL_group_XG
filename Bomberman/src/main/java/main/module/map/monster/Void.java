package main.module.map.monster;

import main.module.map.GameCanvas;
import main.module.map.entities.Entity;

import java.util.Random;

public class Void extends Entity {
    public Void(GameCanvas gc) {
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
        up1 = setup("/Images/monster/Void");
        up2 = setup("/Images/monster/Void");
        down1 = setup("/Images/monster/Void");
        down2 = setup("/Images/monster/Void");
        left1 = setup("/Images/monster/Void");
        left2 = setup("/Images/monster/Void");
        right1 = setup("/Images/monster/Void");
        right2 = setup("/Images/monster/Void");

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
