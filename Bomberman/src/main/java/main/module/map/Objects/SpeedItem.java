package main.module.map.Objects;

import main.module.map.GameCanvas;
import main.module.map.entities.Entity;

public class SpeedItem extends Entity {
    private int effectDuration = 300;
    private int effectCounter = 0;
    private boolean isActive = false;
    private boolean pickedUp = false;
    private int originalSpeed;

    public SpeedItem(GameCanvas gc) {
        super(gc);
        image = setup("/Images/boots");
        solidArea.width = gc.tileSize;
        solidArea.height = gc.tileSize;
        solidAreaDefaultX = 0;
        solidAreaDefaultY = 0;
        collision = true;
        type = 3;
    }

    public void update() {
        if (isActive) {
            effectCounter++;
            if (effectCounter >= effectDuration) {
                gc.player.speed = originalSpeed;
                isActive = false;
                effectCounter = 0;

                for (int i = 0; i < gc.obj.length; i++) {
                    if (gc.obj[i] == this) {
                        gc.obj[i] = null;
                        break;
                    }
                }
            }
        }
    }

    public void applyEffect() {
        if (!isActive) {
            originalSpeed = gc.player.speed;
            gc.player.speed = 6;
            isActive = true;
            pickedUp = true;
            collision = false;
        }
    }
}
