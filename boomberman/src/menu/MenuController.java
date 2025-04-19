package menu;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

import static com.sun.javafx.scene.control.skin.Utils.getResource;

public class MenuController {


    @FXML
    private Button playButton;

    @FXML
    private Button helpButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button settingButton;

    @FXML
    private Button authorsButton;

    @FXML
    void initialize() {

        // Initialize any components here if needed
    }


    @FXML
    private void onPlayButtonClick(ActionEvent event) {
        // Tắt focus (không cho nút giữ focus khi click)
        playButton.setFocusTraversable(false);

        // Hiệu ứng nháy nút
        FadeTransition fade = new FadeTransition(Duration.millis(100), playButton);
        fade.setFromValue(1.0);
        fade.setToValue(0.5);
        fade.setCycleCount(2); // nháy 2 lần
        fade.setAutoReverse(true);
        fade.play();

        // Chuyển focus ra khỏi nút để không hiện chữ đen hay viền
       playButton.getScene().getRoot().requestFocus();

        // Thêm hành động chính ở đây nếu cần
        System.out.println("Nút Play được nhấn");
    }

    @FXML
    public void onHelpButtonClick(ActionEvent event) throws IOException{
        // Tắt focus (không cho nút giữ focus khi click)
        helpButton.setFocusTraversable(false);

        // Hiệu ứng nháy nút
        FadeTransition fade = new FadeTransition(Duration.millis(100), helpButton);
        fade.setFromValue(1.0);
        fade.setToValue(0.3);
        fade.setCycleCount(2); // nháy 2 lần
        fade.setAutoReverse(true);
        fade.play();

        // Chuyển focus ra khỏi nút để không hiện chữ đen hay viền
        helpButton.getScene().getRoot().requestFocus();

        System.out.println("Help button clicked");
        // Add your help screen logic here

        // Load Help.fxml và chuyển scene delay 0.5s
        PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
        delay.setOnFinished(e -> {
            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("screen_2/Help.fxml")));
                Stage stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        delay.play(); // Chạy hiệu ứng delay
    }


    @FXML
    public void onExitButtonClick(ActionEvent event) {
        exitButton.setFocusTraversable(false);

        // Hiệu ứng nháy nút
        FadeTransition fade = new FadeTransition(Duration.millis(100), exitButton);
        fade.setFromValue(1.0);
        fade.setToValue(0.3);
        fade.setCycleCount(2); // nháy 2 lần
        fade.setAutoReverse(true);
        fade.play();

        // Chuyển focus ra khỏi nút để không hiện chữ đen hay viền
        exitButton.getScene().getRoot().requestFocus();

        System.out.println("Exiting game...");
        Platform.exit();
    }

    public void onSettingButtonClick(ActionEvent event) {

        System.out.println("Settings button clicked");
        // Add your settings screen logic here

        PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
        delay.setOnFinished(e -> {
            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("screen_2/Setting.fxml")));
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        delay.play();

    }


   public void onAuthorsButtonClick(ActionEvent event) {
        System.out.println("Authors button clicked");
        // Add your authors screen logic here


       // Load Help.fxml và chuyển scene delay 0.5s
       PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
       delay.setOnFinished(e -> {
           try {
               Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("screen_2/Authors.fxml")));
               Stage stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
               stage.setScene(new Scene(root));
               stage.show();
           } catch (IOException ex) {
               ex.printStackTrace();
           }
       });

       delay.play(); // Chạy hiệu ứng delay
   }

}
