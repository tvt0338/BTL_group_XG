package Main_Modules.collisions;

import Main_Modules.entities.Entity;
import Main_Modules.map01.GameCanvas;

public class CollisionChecker {
    GameCanvas gcCollision;

    public CollisionChecker(GameCanvas gcCollision) {
        this.gcCollision = gcCollision;
    }

    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gcCollision.tileSize;
        int entityRightCol = entityRightWorldX / gcCollision.tileSize;
        int entityTopRow = entityTopWorldY / gcCollision.tileSize;
        int entityBottomRow = entityBottomWorldY / gcCollision.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gcCollision.tileSize;
                tileNum1 = gcCollision.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gcCollision.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (gcCollision.tileM.tile[tileNum1].collision ||
                        gcCollision.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gcCollision.tileSize;
                tileNum1 = gcCollision.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gcCollision.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gcCollision.tileM.tile[tileNum1].collision ||
                        gcCollision.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gcCollision.tileSize;
                tileNum1 = gcCollision.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gcCollision.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                if (gcCollision.tileM.tile[tileNum1].collision ||
                        gcCollision.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gcCollision.tileSize;
                tileNum1 = gcCollision.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gcCollision.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gcCollision.tileM.tile[tileNum1].collision ||
                        gcCollision.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }
}
