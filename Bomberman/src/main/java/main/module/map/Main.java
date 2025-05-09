package main.module.map;

import main.module.map.GameCanvas;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main{

    public void start(Stage primaryStage) {
        GameCanvas gameCanvas = new GameCanvas();

        Group root = new Group();
        root.getChildren().add(gameCanvas);

        Scene scene = new Scene(root, GameCanvas.screenWidth, GameCanvas.screenHeight);
        primaryStage.setTitle("2D game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();

        gameCanvas.setUpGame();
        gameCanvas.startGame();
    }
}
