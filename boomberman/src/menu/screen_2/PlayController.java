package menu.screen_2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PlayController {
    @FXML
    private Label labelDialogue;
    @FXML
    private Button nextButton;

    private final List<String> dialogues = Arrays.asList(
            "Trong một vũ trụ xa xôi, tồn tại một vương quốc hòa bình mang tên \"Terra\", nơi con người và các sinh vật sống chung trong yên bình. Vương quốc được bảo vệ bởi Thần Nhãn (God’s Eye) - một viên bảo thạch thần bí có khả năng xua tan bóng tối và bảo vệ thế giới khỏi thế lực tà ác.",
            "Tuy nhiên, một ngày nọ, Chúa tể Hư Không - Malakar đã tìm ra cách phá vỡ phong ấn của Thần Nhãn. Hắn triệu hồi đội quân quái vật từ cõi Vô Định, phá hủy làng mạc, giam cầm người dân và bao phủ vùng đất bằng bóng tối kinh hoàng.",
            "Chỉ còn lại một hy vọng - Bomber, một chiến binh trẻ tuổi dũng cảm, người được thần linh ban cho sức mạnh . Với trái tim quả cảm và tinh thần bất khuất, Bomber lên đường vượt qua 3 vùng đất chết chóc để tìm lại Thần Nhãn và đánh bại ác ma Malakar, mang lại hòa bình cho Terra."
    );
    private int currentIndex = 0;
    private Timeline timeline;
    private boolean isTyping = false;


    public void initialize() {
        nextButton.setOnAction(e -> handleNext());
        playDialogue(dialogues.get(currentIndex));
    }

    private void playDialogue(String text) {
        labelDialogue.setText("");
        isTyping = true;
        timeline = new Timeline();
        for (int i = 0; i < text.length(); i++) {
            final int index = i;
            KeyFrame kf = new KeyFrame(Duration.millis(30 * i), e -> {
                labelDialogue.setText(text.substring(0, index + 1));
            });
            timeline.getKeyFrames().add(kf);
        }
        timeline.setOnFinished(e -> isTyping = false);
        timeline.play();
    }

    private void handleNext() {
        if (isTyping) {
            // Bỏ qua hiệu ứng, hiển thị toàn bộ
            timeline.stop();
            labelDialogue.setText(dialogues.get(currentIndex));
            isTyping = false;
        } else {
            currentIndex++;
            if (currentIndex < dialogues.size()) {
                playDialogue(dialogues.get(currentIndex));
            } else {
                // Cốt truyện kết thúc => hiệu ứng chuyển sang map1.fxml
                Scene scene = nextButton.getScene();
                Parent currentRoot = scene.getRoot();

                // Hiệu ứng xoay lật cảnh (rotateY)
                javafx.animation.RotateTransition rotateOut = new javafx.animation.RotateTransition(Duration.millis(500), currentRoot);
                rotateOut.setFromAngle(0);
                rotateOut.setToAngle(90);
                rotateOut.setAxis(javafx.geometry.Point3D.ZERO.add(0, 1, 0));

                rotateOut.setOnFinished(event -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/map/map1.fxml"));
                        Parent nextRoot = loader.load();

                        nextRoot.setRotate(-90); // Bắt đầu từ -90 độ để lật vào
                        scene.setRoot(nextRoot); // Gán màn hình mới

                        javafx.animation.RotateTransition rotateIn = new javafx.animation.RotateTransition(Duration.millis(500), nextRoot);
                        rotateIn.setFromAngle(-90);
                        rotateIn.setToAngle(0);
                        rotateIn.setAxis(javafx.geometry.Point3D.ZERO.add(0, 1, 0));
                        rotateIn.play();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

                rotateOut.play();
            }
        }
    }

}

