package org.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import java.util.Random;

public class Balloom {

    private static final int SIZE = 40;  // Kích thước quái
    private static final int MOVEMENT_SPEED = 1;  // Tốc độ di chuyển
    private int hp = 1;  // HP của Balloom
    private ImageView imageView;  // Hình ảnh của quái
    private int x, y;  // Vị trí của quái
    private Random random;  // Để di chuyển ngẫu nhiên

    public Balloom(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.random = new Random();

        // Tạo hình ảnh Balloom (có thể thay ảnh khác)
        this.imageView = new ImageView(
                new Image(getClass().getResource("/balloom.jpg").toExternalForm())
        );

        this.imageView.setFitWidth(SIZE);
        this.imageView.setFitHeight(SIZE);
        this.imageView.setX(x);
        this.imageView.setY(y);

        // Di chuyển Balloom ngẫu nhiên
        moveRandomly();
    }

    // Di chuyển ngẫu nhiên
    private void moveRandomly() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    int direction = random.nextInt(4);  // 0 - lên, 1 - xuống, 2 - trái, 3 - phải
                    switch (direction) {
                        case 0: // Up
                            y -= MOVEMENT_SPEED;
                            break;
                        case 1: // Down
                            y += MOVEMENT_SPEED;
                            break;
                        case 2: // Left
                            x -= MOVEMENT_SPEED;
                            break;
                        case 3: // Right
                            x += MOVEMENT_SPEED;
                            break;
                    }
                    imageView.setX(x);
                    imageView.setY(y);
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);  // Lặp lại vô hạn
        timeline.play();
    }

    // Lấy vị trí hình ảnh của Balloom
    public ImageView getImageView() {
        return imageView;
    }

    // Giảm HP khi Balloom bị tấn công
    public void takeDamage() {
        hp--;
        if (hp <= 0) {
            // Balloom chết (có thể thêm hiệu ứng hoặc hành động khi chết)
            imageView.setVisible(false);  // Ẩn hình ảnh Balloom
        }
    }
}
