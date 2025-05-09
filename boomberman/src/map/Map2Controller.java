package map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import map2.Map2;
import menu.music.music_map2;
import menu.music.music_map3;

import java.io.IOException;

public class Map2Controller {

    @FXML private Label label1;
    @FXML private Label label2;
    @FXML private Label label3;
    @FXML private ImageView gif1, gif2, gif3;
    @FXML private Button next;
    @FXML private Button startButton;
    @FXML private ImageView imageView;
    @FXML private ImageView iamg;

    // Tách TEXT1 thành 3 đoạn ứng với 3 gif
    private final String PART1 = "      Tiến vào Thành Trì Bóng Tối, nơi quái vật tụ tập dưới sự chỉ huy của Tướng Quỷ Argon. Đây là trung tâm điều khiển đội quân ma quái của Malakar. Bomber phải đối đầu với những tên quái mạnh hơn.\n";
    private final String PART2 = "      Bomber phải tận dụng các vật phẩm hỗ trợ như God's Eye để nhìn thấu Void, và sử dụng Skyfall để tiêu diệt những tên Fly đáng ghét, sau đó đi vào portal để tiếp tục chiến đấu với boss.\n";
    private final String PART3 = "      Nhiệm vụ của bạn là hãy sử dụng các phím được hỗ trợ để điều khiển Bomber tiêu diệt những kẻ thù để thắp lại ánh sáng cho thành trì. \n";

    private final String TEXT2 = "🐉 Kẻ thù :\n" +
            "•   Fly\n" +
            "•   Void\n"+
            "•   Tanker\n";

    private final String TEXT3 = "💡 Mục tiêu:\n" +
            "•   Tiêu diệt toàn bộ quái vật.\n" +
            "•   Tìm và đi qua Portal.";

    private final int SPEED = 20;

    private Timeline timeline;
    private Timeline timelineLabel2;
    private Timeline timelineLabel3;

    public void initialize() {
        gif1.setVisible(false);
        gif2.setVisible(false);
        gif3.setVisible(false);
        startButton.setVisible(false);
        startTyping();
        startTypingLabel2();
        startTypingLabel3();
    }

    public void startTyping() {
        timeline = new Timeline();
        String fullText = PART1 + PART2 + PART3;

        for (int i = 0; i <= fullText.length(); i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(i * SPEED),
                    event -> {
                        label1.setText(fullText.substring(0, index));

                        // Hiện gif1 khi PART1 hoàn thành
                        if (index == PART1.length()) {
                            gif1.setVisible(true);
                        }

                        // Hiện gif2 khi PART2 hoàn thành
                        if (index == (PART1 + PART2).length()) {
                            gif2.setVisible(true);
                        }

                        // Hiện gif3 khi toàn bộ TEXT1 hoàn thành
                        if (index == fullText.length()) {
                            gif3.setVisible(true);
                        }
                    }
            );
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.play();
    }

    public void startTypingLabel2() {
        timelineLabel2 = new Timeline();

        for (int i = 0; i <= TEXT2.length(); i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis((PART1.length() + PART2.length() + PART3.length()) * SPEED + i * SPEED),
                    event -> label2.setText(TEXT2.substring(0, index))
            );
            timelineLabel2.getKeyFrames().add(keyFrame);
        }

        timelineLabel2.play();
    }

    public void startTypingLabel3() {
        timelineLabel3 = new Timeline();

        for (int i = 0; i <= TEXT3.length(); i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis((PART1.length() + PART2.length() + PART3.length() + TEXT2.length()) * SPEED + i * SPEED),
                    event -> {
                        label3.setText(TEXT3.substring(0, index));
                        if (index == TEXT3.length()) {
                            startButton.setVisible(true);
                        }
                    }
            );
            timelineLabel3.getKeyFrames().add(keyFrame);
        }

        timelineLabel3.play();
    }

    @FXML
    private void onNextButtonClicked() {
        if (timeline != null) timeline.stop();
        if (timelineLabel2 != null) timelineLabel2.stop();
        if (timelineLabel3 != null) timelineLabel3.stop();

        // Hiện toàn bộ nội dung và ảnh
        label1.setText(PART1 + PART2 + PART3);
        label2.setText(TEXT2);
        label3.setText(TEXT3);

        gif1.setVisible(true);
        gif2.setVisible(true);
        gif3.setVisible(true);

        imageView.setVisible(true);
        startButton.setVisible(true);
        iamg.setVisible(false);
        next.setVisible(false);
    }

    @FXML
    private void onStartButtonClicked() {
        // Chuyển từ màn hình intro sang màn hình Map1
        startGame();
    }

    public void startGame() {
        // Chuyển sang màn hình game Map1 khi nhấn nút Start
        Stage currentStage = (Stage) startButton.getScene().getWindow(); // Lấy Stage hiện tại
        Map2 map1 = new Map2(); // Tạo một đối tượng Map1 mới

        try {
            map1.start(currentStage); // Gọi start() của Map1 và truyền vào Stage hiện tại
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onPortalEntered() {
        // Dừng nhạc map2 và phát nhạc map3
        music_map2.stopMusic();
        music_map3.playMusic();

        try {
            // Chuyển sang map3
            FXMLLoader loader = new FXMLLoader(getClass().getResource("map3.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) scene.getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
