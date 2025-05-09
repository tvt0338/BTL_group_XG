package main.module.map.Objects;

import javafx.scene.canvas.GraphicsContext;
import main.module.map.GameCanvas;
import main.module.map.entities.Entity;
import main.module.map.tile.TileManager;

import java.awt.*;

public class OBJ_Bomb extends Entity {
    private int explodeCounter = 0;
    private boolean exploded = false;
    private final int explodeTime = 150;
    private final int explosionDisplayTime = 50;
    private final int range;

    public OBJ_Bomb(GameCanvas gc) {
        super(gc);
        this.range = gc.player.bombRange;
        image = setup("/Images/Bombs/bomb");
        image2 = setup("/Images/Bombs/explode_center");
        image3 = setup("/Images/Bombs/explode_depth");
        image4 = setup("/Images/Bombs/explode_width");

        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = gc.tileSize;
        solidArea.height = gc.tileSize;
        solidAreaDefaultX = 0;
        solidAreaDefaultY = 0;

        collision = true;
        type = 2;
    }

    public void update() {
        if (!exploded) {
            explodeCounter++;
            if (explodeCounter >= explodeTime) {
                exploded();
                exploded = true;
                collision = false;
            }
        } else {
            explodeCounter++;
        }
    }

    public void exploded() {
        TileManager tileM = gc.tileM;
        int tileSize = gc.tileSize;

        int[] directionX = {0,0,-tileSize, tileSize, -tileSize * 2, tileSize * 2};
        int[] directionY = {-tileSize,tileSize,0, 0,0,0};

        for (int i = 0; i < 4; i++) {
            int checkX = worldX + directionX[i];
            int checkY = worldY + directionY[i];
            int col = checkX / tileSize;
            int row = checkY / tileSize;

            if (col >= 0 && col < gc.maxWorldCol && row >= 0 && row < gc.maxWorldRow) {
                if (tileM.mapTileNum[col][row] == 3 &&
                        (i==0 || Math.abs(directionX[i]) <= tileSize * range ||
                                Math.abs(directionY[i]) <= tileSize * range)) {
                    tileM.mapTileNum[col][row] = 2;
                }
            }

            checkCollision(gc.player);
            for (Entity monster : gc.monsters) {
                if (monster != null) {
                    checkCollision(monster);
                }
            }
        }
    }

    public void checkCollision(Entity target) {
        int tileSize = gc.tileSize;
        Rectangle explosionArea = new Rectangle(worldX - tileSize * range, worldY - tileSize * range,
                tileSize * (2 * range + 1), tileSize * (2 * range + 1));
        target.solidArea.x = target.worldX + target.solidArea.x;
        target.solidArea.y = target.worldY + target.solidArea.y;

        if (explosionArea.intersects(target.solidArea)) {
            if (target.life > 0 && target.type != 0) {
                target.life--;
                System.out.println(target.getClass().getSimpleName() + " hit by bomb, life: " + target.life);
            }
        }

        target.solidArea.x = target.solidAreaDefaultX;
        target.solidArea.y = target.solidAreaDefaultY;
    }

//    private int findBombIndex() {
//        for (int i = 0; i < gc.obj.length; i++) {
//            if (gc.obj[i] == this) {
//                return i;
//            }
//        }
//        return -1;
//    }


    public int getExplodeCounter() {
        return explodeCounter;
    }

    public int getExplodeTime() {
        return explodeTime;
    }

    public boolean isExploded() {
        return exploded;
    }

    public void draw(GraphicsContext g2) {
        int screenX = worldX - gc.player.worldX + gc.player.screenX;
        int screenY = worldY - gc.player.worldY + gc.player.screenY;

        if (worldX + gc.tileSize > gc.player.worldX - gc.player.screenX &&
                worldX - gc.tileSize < gc.player.worldX + gc.player.screenX &&
                worldY + gc.tileSize > gc.player.worldY - gc.player.screenY &&
                worldY - gc.tileSize < gc.player.worldY + gc.player.screenY) {

            if (!exploded) {
                g2.drawImage(image, screenX, screenY, gc.tileSize, gc.tileSize);
            } else if (explodeCounter <= explodeTime + explosionDisplayTime){
                g2.drawImage(image2, screenX, screenY, gc.tileSize, gc.tileSize);

                for (int i = 0; i <= range; i++) {
                    if (screenY - i * gc.tileSize >= 0
                    && screenY - i * gc.tileSize <= GameCanvas.screenHeight) {
                        g2.drawImage(image3, screenX, screenY - i * gc.tileSize, gc.tileSize, gc.tileSize);
                    }
                    if (screenY + i * gc.tileSize >= 0 &&
                            screenY + i * gc.tileSize <= GameCanvas.screenHeight) {
                        g2.drawImage(image3, screenX, screenY + i * gc.tileSize, gc.tileSize, gc.tileSize);
                    }
                }

                for (int i = 1; i <= range; i++) {
                    if (screenX - i * gc.tileSize >= 0 &&
                            screenX - i * gc.tileSize <= GameCanvas.screenWidth) {
                        g2.drawImage(image4, screenX - i * gc.tileSize, screenY, gc.tileSize, gc.tileSize);
                    }
                    if (screenX + i * gc.tileSize >= 0 &&
                            screenX + i * gc.tileSize <= GameCanvas.screenWidth) {
                        g2.drawImage(image4, screenX + i * gc.tileSize, screenY, gc.tileSize, gc.tileSize);
                    }
                }
            }

        }
    }
}
