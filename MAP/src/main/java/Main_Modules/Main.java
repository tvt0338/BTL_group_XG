package Main_Modules;

import Main_Modules.map01.GameCanvas;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GameCanvas gameCanvas = new GameCanvas();

        Group root = new Group();
        root.getChildren().add(gameCanvas);

        Scene scene = new Scene(root, GameCanvas.screenWidth, GameCanvas.screenHeight);
        primaryStage.setTitle("2D game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        gameCanvas.startGame();
    }
}
