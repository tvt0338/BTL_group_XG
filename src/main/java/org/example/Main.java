package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

public class Main extends Application {

    private Bomber bomber;
    private Balloom balloom;
    private Oneal oneal;
    private Fly fly;

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        // ⚠️ Đặt đúng thứ tự khởi tạo
        bomber = new Bomber(100, 100);           // ✅ Tạo bomber trước
        balloom = new Balloom(200, 100);
        oneal = new Oneal(300, 200, bomber);
        fly = new Fly(150, 300, bomber);

        // Thêm tất cả vào scene
        root.getChildren().addAll(
                bomber.getImageView(),
                balloom.getImageView(),
                oneal.getImageView(),
                fly.getImageView()
        );

        // Game loop cho Oneal & Fly (Balloom dùng Timeline riêng)
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                oneal.update();
                fly.update();

                // Cập nhật vị trí
                oneal.getImageView().setX(oneal.getX());
                oneal.getImageView().setY(oneal.getY());

                fly.getImageView().setX(fly.getX());
                fly.getImageView().setY(fly.getY());
            }
        };
        gameLoop.start();

        Scene scene = new Scene(root, 640, 480);
        primaryStage.setTitle("Bomberman: Test Nhân Vật");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
