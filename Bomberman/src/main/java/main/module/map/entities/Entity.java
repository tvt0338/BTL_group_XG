package main.module.map.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.module.map.GameCanvas;
import main.module.map.Utils.ImageScaler;

import java.awt.*;
import java.io.InputStream;

public class Entity {
    public GameCanvas gc;
    public int worldX, worldY;
    public int speed = 8;

    public Image up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction = "down";

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea = new Rectangle(0, 0, 32, 32);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public int actionLockCounter = 0;
    public boolean collisionOn = false;
    public boolean invincible = false;
    public int invincibleCounter = 0;

    public Image image, image2, image3, image4;
    public String name;
    public boolean collision = false;
    public int type; //0 = player, 1 = monster, 2= bomb, 3 = items

    //CHARACTER STATUS
    public int maxLife;
    public int life;

    public Entity(GameCanvas gc) {
        this.gc = gc;
    }

    public void setAction() {}
    public void update() {
        setAction();

        collisionOn = false;
        gc.cChecker.checkTile(this);
        gc.cChecker.checkObject(this, false);
        gc.cChecker.checkEntity(this, gc.monsters);
        if (type == 1) {
            gc.cChecker.checkBomb(this);
        }
//        gc.cChecker.checkPlayer(this);
        boolean contactPlayer = gc.cChecker.checkPlayer(this);

        if (this.type == 1 &&  contactPlayer) {
            if (!gc.player.invincible) {
                gc.player.life--;
                gc.player.invincible = true;
            }
        }

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
    public void draw(GraphicsContext graphicsContext) {
        int screenX = worldX - gc.player.worldX + gc.player.screenX;
        int screenY = worldY - gc.player.worldY + gc.player.screenY;

        if (worldX + gc.tileSize > gc.player.worldX - gc.player.screenX &&
                worldX - gc.tileSize < gc.player.worldX + gc.player.screenX &&
                worldY + gc.tileSize > gc.player.worldY - gc.player.screenY &&
                worldY - gc.tileSize < gc.player.worldY + gc.player.screenY) {

            Image imageToDraw = null;

            // Vẽ items (type == 3)
            if (type == 3) {
                imageToDraw = image;
            } else {
                // Vẽ animation cho player hoặc quái vật (type == 0 hoặc 1)
                switch (direction) {
                    case "up":
                        if (spriteNum == 1) {
                            imageToDraw = up1;
                        }
                        if (spriteNum == 2) {
                            imageToDraw = up2;
                        }
                        break;
                    case "down":
                        if (spriteNum == 1) {
                            imageToDraw = down1;
                        }
                        if (spriteNum == 2) {
                            imageToDraw = down2;
                        }
                        break;
                    case "left":
                        if (spriteNum == 1) {
                            imageToDraw = left1;
                        }
                        if (spriteNum == 2) {
                            imageToDraw = left2;
                        }
                        break;
                    case "right":
                        if (spriteNum == 1) {
                            imageToDraw = right1;
                        }
                        if (spriteNum == 2) {
                            imageToDraw = right2;
                        }
                        break;
                }
            }

            if (imageToDraw != null) {
                graphicsContext.drawImage(imageToDraw, screenX, screenY, gc.tileSize, gc.tileSize);
            } else {
                System.out.println("ImageToDraw is null for type " + type + " at (" + worldX + ", " + worldY + ")");
            }
        }
    }
    public Image setup(String imagePath) {
        ImageScaler imageScaler = new ImageScaler();
        Image image = null;
        try {
            InputStream is = getClass().getResourceAsStream( imagePath + ".png");
            if (is != null) {
                Image original = new Image(is);
                image = imageScaler.scale(original, gc.tileSize, gc.tileSize); // Hàm scale của bạn
            } else {
                System.out.println("Không tìm thấy ảnh: " + imagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return image;
    }
}
