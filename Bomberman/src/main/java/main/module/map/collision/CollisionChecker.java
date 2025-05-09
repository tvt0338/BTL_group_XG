package main.module.map.collision;

import main.module.map.Objects.OBJ_Bomb;
import main.module.map.entities.Entity;
import main.module.map.GameCanvas;

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

    public int checkObject(Entity entity, boolean player) {
        int index = 999;

        for (int i = 0; i < gcCollision.obj.length; i++) {
            if (gcCollision.obj[i] != null) {
                // get entity's solidArea position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // get object's solidArea position
                gcCollision.obj[i].solidArea.x = gcCollision.obj[i].worldX + gcCollision.obj[i].solidArea.x;
                gcCollision.obj[i].solidArea.y = gcCollision.obj[i].worldY + gcCollision.obj[i].solidArea.y;

                switch (entity.direction) {
                    case "up": entity.solidArea.y -= entity.speed; break;
                    case "down":entity.solidArea.y += entity.speed;break;
                    case "left":entity.solidArea.x -= entity.speed;break;
                    case "right":entity.solidArea.x += entity.speed;break;
                }
                if (entity.solidArea.intersects(gcCollision.obj[i].solidArea)) {
                    if(gcCollision.obj[i].collision) {
                        entity.collisionOn = true;
                    }
                    if (player) {
                        index = i;
                    }

                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gcCollision.obj[i].solidArea.x = gcCollision.obj[i].solidAreaDefaultX;
                gcCollision.obj[i].solidArea.y = gcCollision.obj[i].solidAreaDefaultY;
            }
        }
        return index;
    }

    //NPC Or Monster.
    public int checkEntity(Entity entity, Entity[] target) {
        int index = 999;

        for (int i = 0; i < target.length; i++) {
            if (target[i] != null) {
                // get entity's solidArea position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // get object's solidArea position
                target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
                target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;

                switch (entity.direction) {
                    case "up": entity.solidArea.y -= entity.speed;break;
                    case "down":entity.solidArea.y += entity.speed;break;
                    case "left":entity.solidArea.x -= entity.speed;break;
                    case "right":entity.solidArea.x += entity.speed;break;
                }
                if (entity.solidArea.intersects(target[i].solidArea)) {
                    if (target[i] != entity) {
                        entity.collisionOn = true;
                        index = i;
                    }

                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }
        }
        return index;
    }

    public boolean checkPlayer(Entity entity) {
        boolean contactPlayer = false;
        // get entity's solidArea position
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;

        // get object's solidArea position
        gcCollision.player.solidArea.x = gcCollision.player.worldX + gcCollision.player.solidArea.x;
        gcCollision.player.solidArea.y = gcCollision.player.worldY + gcCollision.player.solidArea.y;

        switch (entity.direction) {
            case "up": entity.solidArea.y -= entity.speed;break;
            case "down": entity.solidArea.y += entity.speed; break;
            case "left": entity.solidArea.x -= entity.speed; break;
            case "right": entity.solidArea.x += entity.speed; break;
        }
        if (entity.solidArea.intersects(gcCollision.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gcCollision.player.solidArea.x = gcCollision.player.solidAreaDefaultX;
        gcCollision.player.solidArea.y = gcCollision.player.solidAreaDefaultY;

        return contactPlayer;
    }

    public void checkBomb(Entity entity) {
        for (OBJ_Bomb bomb : gcCollision.bombs) {
            if (bomb != null && !bomb.isExploded()) { // Chỉ kiểm tra bomb chưa nổ
                // get entity's solidArea position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // get bomb's solidArea position
                bomb.solidArea.x = bomb.worldX + bomb.solidArea.x;
                bomb.solidArea.y = bomb.worldY + bomb.solidArea.y;

                switch (entity.direction) {
                    case "up": entity.solidArea.y -= entity.speed; break;
                    case "down": entity.solidArea.y += entity.speed; break;
                    case "left": entity.solidArea.x -= entity.speed; break;
                    case "right": entity.solidArea.x += entity.speed; break;
                }

                if (entity.solidArea.intersects(bomb.solidArea)) {
                    if (bomb.collision) {
                        entity.collisionOn = true;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                bomb.solidArea.x = bomb.solidAreaDefaultX;
                bomb.solidArea.y = bomb.solidAreaDefaultY;
            }
        }
    }
}
