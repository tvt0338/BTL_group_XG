package main.module.map.Objects;

import javafx.scene.image.Image;
import main.module.map.GameCanvas;
import main.module.map.entities.Entity;

import java.awt.*;

public class OBJ_Heart extends Entity {
    public OBJ_Heart (GameCanvas gc) {
        super(gc);
        name = "heart";

        image = setup("/Objects/heart_full");
        image2 = setup("/Objects/heart_half");
        image3 = setup("/Objects/heart_blank");

        collision = false;

        solidArea.x = 2;
        solidArea.y = 8;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 28;
        solidArea.height = 24;


    }
}
