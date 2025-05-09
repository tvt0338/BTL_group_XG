package map;

import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
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
import menu.music.music_map3;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Map3Controller {
    @FXML
    private Label labelDialogue;
    @FXML
    private Button nextButton;
    @FXML
    private Button startButton;
    @FXML
    private ImageView imageView;
    @FXML
    private ImageView iamg;

    private final List<String> dialogues = Arrays.asList(
            "Sau khi hạ gục đoàn quân của tướng quỷ Argon , hắn chạy về báo cáo với Malakar, Malakar tức giận và đã giết chết Argon rồi luyện hóa hắn.",
            "Tuy nhiên đang trong quá trình Malakar hấp thu Argon thì Bomber đã đi vào và tặng cho chúng một quả boom ",
            "Sau đó hàng ngàn quái vật nhỏ ùa ra tấn công bomber để trả thù cho chủ của chúng . Hãy ấn tiếp theo để theo dõi trận chiến đó ngay nào . "
    );

    private int currentIndex = 0;
    private Timeline timeline;
    private boolean isTyping = false;

    public void initialize() {
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

    @FXML
    private void onNextButtonClicked() {
        if (isTyping) {
            // Nếu đang đánh máy, thì hiển thị toàn bộ nội dung và dừng hiệu ứng
            timeline.stop();
            labelDialogue.setText(dialogues.get(currentIndex));
            isTyping = false;
        } else {
            // Nếu đã hiện xong đoạn này, thì chuyển sang đoạn tiếp theo
            currentIndex++;
            if (currentIndex < dialogues.size()) {
                playDialogue(dialogues.get(currentIndex));
            } else {
                // Nếu đã là đoạn cuối, thì hiện startButton, ẩn nextButton và hình ảnh cũ
                imageView.setVisible(true);
                startButton.setVisible(true);
                iamg.setVisible(false);
                nextButton.setVisible(false);
            }
        }
    }

    @FXML
    private void onStartButtonClicked() {
        // Chuyển từ màn hình intro sang màn hình Map1
        startGame();
    }

    public void startGame() {
        // Chuyển sang màn hình game Map3 khi nhấn nút Start
        Stage currentStage = (Stage) startButton.getScene().getWindow();
        map3.Map3 map3 = new map3.Map3();

        try {
            map3.start(currentStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onPortalEntered() {
        // Dừng nhạc map3 khi kết thúc game
        music_map3.stopMusic();

        try {
            // Chuyển về màn hình kết thúc hoặc màn hình chính
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/menu/Menu.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) scene.getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
