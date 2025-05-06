package Main_Modules.entities;

import Main_Modules.KeyHandler;
import Main_Modules.map01.GameCanvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.*;
import java.security.Key;

public class Player extends Entity{
    private GameCanvas gameCanvas;
    private KeyHandler keyH;

    public final int screenX;
    public final int screenY;


    public Player(GameCanvas gameCanvas, KeyHandler keyH) {
        this.gameCanvas = gameCanvas;
        this.keyH = keyH;

        screenX = GameCanvas.screenWidth / 2 -(GameCanvas.tileSize / 2);
        screenY = GameCanvas.screenHeight / 2 -(GameCanvas.tileSize / 2);

        solidArea = new Rectangle();
        solidArea.x = 4;
        solidArea.y = 8;
        solidArea.width = 24;
        solidArea.height = 24;

        setDefaultValue();
        getPlayerImage();


    }

    public void setDefaultValue() {
        worldX = 250;
        worldY = 150;
        speed = 4;
        direction = "down";

    }

    public void getPlayerImage() {
        up1 = new Image(getClass().getResourceAsStream("/Images/player/boy_up_1.png"));
        up2 = new Image(getClass().getResourceAsStream("/Images/player/boy_up_2.png"));
        down1 = new Image(getClass().getResourceAsStream("/Images/player/boy_down_1.png"));
        down2 = new Image(getClass().getResourceAsStream("/Images/player/boy_down_2.png"));
        left1 = new Image(getClass().getResourceAsStream("/Images/player/boy_left_1.png"));
        left2 = new Image(getClass().getResourceAsStream("/Images/player/boy_left_2.png"));
        right1 = new Image(getClass().getResourceAsStream("/Images/player/boy_right_1.png"));
        right2 = new Image(getClass().getResourceAsStream("/Images/player/boy_right_2.png"));
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
            gameCanvas.cChecker.checkTile(this);

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
    }

    public void draw(GraphicsContext g1) {
//        g1.setFill(Color.WHITE);
//        g1.fillRect(x, y, gameCanvas.tileSize, gameCanvas.tileSize);

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

        g1.drawImage(image, screenX, screenY, gameCanvas.tileSize, gameCanvas.tileSize);
    }
}
