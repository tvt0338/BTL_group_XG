package main.module.map;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;

public class KeyHandler {
    public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed;
    GameCanvas gc;

    public KeyHandler(GameCanvas gc) {
        this.gc = gc;
        setUpInputHandling(gc);
    }

    public void setUpInputHandling(Node target) {
        target.setOnKeyPressed(event -> keyPressed(event.getCode()));
        target.setOnKeyReleased(event -> keyReleased(event.getCode()));

        target.setFocusTraversable(true);
        target.requestFocus();
    }

    public void keyPressed(KeyCode e) {
        switch (e) {
            case W:upPressed = true; break;
            case S:downPressed = true; break;
            case A:leftPressed = true; break;
            case D:rightPressed = true; break;
            case P:
                if (gc.gameState == gc.playState) {
                    gc.gameState = gc.pauseState;
                } else if (gc.gameState == gc.pauseState) {
                    gc.gameState = gc.playState;
                }
                break;
            case SPACE: spacePressed = true; break;
        }
    }

    public void keyReleased(KeyCode e) {
        switch (e) {
            case W:upPressed = false; break;
            case S:downPressed = false; break;
            case A:leftPressed = false; break;
            case D:rightPressed = false; break;
            case SPACE : spacePressed = false; break;
        }
    }
}
