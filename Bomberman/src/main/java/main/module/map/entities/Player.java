package main.module.map.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import main.module.map.GameCanvas;
import main.module.map.KeyHandler;

import java.awt.Rectangle;
import javafx.scene.text.Font;
import main.module.map.Objects.OBJ_Bomb;
import main.module.map.Objects.RangeItem;
import main.module.map.Objects.SpeedItem;

public class Player extends Entity {
    private KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public int bombRange = 2;


    public Player(GameCanvas gc, KeyHandler keyH) {
        super(gc);
        this.keyH = keyH;

        screenX = GameCanvas.screenWidth / 2 -(gc.tileSize / 2);
        screenY = GameCanvas.screenHeight / 2 -(gc.tileSize / 2);

        solidArea = new Rectangle();
        solidArea.x = 4;
        solidArea.y = 8;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 24;
        solidArea.height = 24;
        type = 0;

        setDefaultValue();
        getPlayerImage();


    }

    public void setDefaultValue() {
        worldX = 25;
        worldY = 25;
        speed = 4;
        direction = "down";

        //PLAYER STATUS
        maxLife = 6;
        life = maxLife;

    }

    public void getPlayerImage() {
//        up1 = new Image(getClass().getResourceAsStream("/Images/player/boy_up_1.png"));
//        up2 = new Image(getClass().getResourceAsStream("/Images/player/boy_up_2.png"));
//        down1 = new Image(getClass().getResourceAsStream("/Images/player/boy_down_1.png"));
//        down2 = new Image(getClass().getResourceAsStream("/Images/player/boy_down_2.png"));
//        left1 = new Image(getClass().getResourceAsStream("/Images/player/boy_left_1.png"));
//        left2 = new Image(getClass().getResourceAsStream("/Images/player/boy_left_2.png"));
//        right1 = new Image(getClass().getResourceAsStream("/Images/player/boy_right_1.png"));
//        right2 = new Image(getClass().getResourceAsStream("/Images/player/boy_right_2.png"));
        up1 = setup("/Images/player/boy_up_1");
        up2 = setup("/Images/player/boy_up_2");
        down1 = setup("/Images/player/boy_down_1");
        down2 = setup("/Images/player/boy_down_2");
        left1 = setup("/Images/player/boy_left_1");
        left2 = setup("/Images/player/boy_left_2");
        right1 = setup("/Images/player/boy_right_1");
        right2 = setup("/Images/player/boy_right_2");
    }

    public void update() {
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            if (keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else {
                direction = "right";
            }

            //Check Tile Collision
            collisionOn = false;
            gc.cChecker.checkTile(this);

            //Check object collision
            int objIndex = gc.cChecker.checkObject(this, true);
            pickUpObj(objIndex);

            //Check NPC collision.
            int monsterIndex = gc.cChecker.checkEntity(this, gc.monsters);
            touchMonster(monsterIndex);

            //IF COLLISIONON is FALSE_>MOVEABLE
            if (!collisionOn) {
                switch (direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }
            spriteCounter++;
            if (spriteCounter > 10) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }

        if (keyH.spacePressed) {
            placeBomb();
        }

        if (invincible == true) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if (life <= 0) {
            gc.gameState = gc.gameOverState;
        }
    }

    private void placeBomb() {
        int bombX = Math.round(((float) worldX / gc.tileSize)) * gc.tileSize;
        int bombY = Math.round(((float) worldY / gc.tileSize)) * gc.tileSize;

        if (!hasBombAtPosition(bombX, bombY)) {
            OBJ_Bomb bomb = new OBJ_Bomb(gc);
            bomb.worldX = bombX;
            bomb.worldY = bombY;
            gc.bombs.add(bomb);
        }
    }

    private boolean hasBombAtPosition(int bombX, int bombY) {
        for (OBJ_Bomb bomb  : gc.bombs) {
            if (bomb != null) {
                int objTileX = bomb.worldX / gc.tileSize;
                int objTileY = bomb.worldY / gc.tileSize;
                int bombTileX = bombX / gc.tileSize;
                int bombTileY = bombY / gc.tileSize;
                if (objTileX == bombTileX && objTileY == bombTileY) {
                    return true;
                }
            }
        }
        return false;
    }
    public void pickUpObj(int i) {
        if (i != 999) {
            if (gc.obj[i] instanceof SpeedItem) {
                ((SpeedItem) gc.obj[i]).applyEffect();
            } else if (gc.obj[i] instanceof RangeItem) {
                ((RangeItem) gc.obj[i]).applyEffect();
            }
        }
    }

    public void touchMonster(int i) {
        if (i != 999) {
            System.out.println("touched an npc");
            if (invincible == false) {
                life--;
                invincible = true;
            }

        }
    }

    public void draw(GraphicsContext g1) {
//        g1.setFill(Color.WHITE);
//        g1.fillRect(x, y, gc.tileSize, gc.tileSize);

        Image image = null;

        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                break;
        }

        if (invincible) {
            g1.setGlobalAlpha(0.3);
        }
        g1.drawImage(image, screenX, screenY);
        g1.setGlobalAlpha(1.0);

        //Debug
//        g1.setFont(new javafx.scene.text.Font("Arial", 26));
//        g1.setFill(Color.BLACK);
//        g1.fillText("Invincible:" + invincibleCounter, 10, 400);

    }
}
