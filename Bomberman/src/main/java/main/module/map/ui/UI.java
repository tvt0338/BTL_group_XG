package main.module.map.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import main.module.map.GameCanvas;
import main.module.map.Objects.OBJ_Heart;
import main.module.map.entities.Entity;

public class UI {
    GameCanvas gcas;
    GraphicsContext gUI;
    Image heart_full, heart_half, heart_blank;
    Font arial_40;

    public UI(GameCanvas gcas) {
        this.gcas = gcas;
        arial_40 = new Font("Arial", 40);
//        OBJ_Key key = new OBJ_Key(gcas);
//        keyImage = key.image;

        //CREATE HUD OBJECTS
        Entity heart = new OBJ_Heart(gcas);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
    }

    public void draw(GraphicsContext gUI) {
        this.gUI = gUI;
        gUI.setFont(arial_40);
        gUI.setFill(Color.WHITE);

        //PLAY STATE
        if (gcas.gameState == gcas.playState) {
            //Do playState stuff later
            drawPlayerLife();
        }
        if (gcas.gameState == gcas.pauseState) {
            drawPlayerLife();
            drawPauseScreen();
        }
        if (gcas.gameState == gcas.gameOverState) {
            drawGameOverSCreen();
        }
    }

    public void drawGameOverSCreen() {
        gUI.setFill(Color.BLACK);
        gUI.setGlobalAlpha(0.5);
        gUI.fillRect(0, 0, gcas.screenWidth, gcas.screenHeight);

        gUI.setGlobalAlpha(1.0);

        Font font = Font.font("Arial", FontWeight.BOLD, 50);
        gUI.setFont(font);

        int x;
        int y;
        String text;

        text = "GAME OVER";

        //SHADOW
        gUI.setFill(Color.BLACK);
        x = getXforCenteredText(text);
        y = gcas.tileSize * 4;
        gUI.fillText(text, x, y);
        //MAIN
        gUI.setFill(Color.WHITE);
        gUI.fillText(text, x - 4, y - 4);

        //RETRY
        Font font1 = Font.font("Arial", 20);
        text = "RETRY";
        x = getXforCenteredText(text);
        y += gcas.tileSize * 8;
        gUI.fillText(text, x, y);

        //Back to menu
        text = "QUIT";
        x = getXforCenteredText(text);
        y += 55;
        gUI.fillText(text, x, y);

    }

    public void drawPlayerLife() {

        int x = gcas.tileSize /2;
        int y = gcas.tileSize /2;
        int i = 0;

        //DRAW BLANK HEART
        while (i < gcas.player.maxLife / 2) {
            gUI.drawImage(heart_blank, x, y,gcas.tileSize, gcas.tileSize);
            i++;
            x += gcas.tileSize;
        }

        x = gcas.tileSize /2;
        y = gcas.tileSize /2;
        i = 0;

        //DRAW CURRENT LIFE
        while (i < gcas.player.life) {
            gUI.drawImage(heart_half,x,y, gcas.tileSize, gcas.tileSize);
            i++;
            if (i < gcas.player.life) {
                gUI.drawImage(heart_full, x, y);
            }
            i++;
            x += gcas.tileSize;
        }
    }
    public void drawPauseScreen() {
        String text = "PAUSE";
        int x = getXforCenteredText(text);
        int y = gcas.screenHeight / 2;
        gUI.fillText(text, x, y);
    }

    public int getXforCenteredText(String text) {
        Text tempText = new Text(text);
        tempText.setFont(arial_40);
        double textWidth = tempText.getLayoutBounds().getWidth();
        return (int) (gcas.screenWidth / 2 - textWidth / 2);
    }
}
