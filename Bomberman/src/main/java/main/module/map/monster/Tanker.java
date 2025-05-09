package main.module.map.monster;

import main.module.map.GameCanvas;
import main.module.map.entities.Entity;

import java.util.Random;

public class Tanker extends Entity {
    public Tanker(GameCanvas gc) {
        super(gc);
        type = 1;
        name = "balloon";
        speed = 1;
        maxLife = 3;
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
        up1 = setup("/Images/monster/Tanker");
        up2 = setup("/Images/monster/Tanker");
        down1 = setup("/Images/monster/Tanker");
        down2 = setup("/Images/monster/Tanker");
        left1 = setup("/Images/monster/Tanker");
        left2 = setup("/Images/monster/Tanker");
        right1 = setup("/Images/monster/Tanker");
        right2 = setup("/Images/monster/Tanker");

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
