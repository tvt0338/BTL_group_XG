package org.example.setting;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingController {
    @FXML
    private Button returnToScreenButton;
    @FXML
    private Label audioLabel;
    @FXML
    private Slider audioSlider;
    @FXML
    private ImageView audioImage;

    int percentage;

    public void initialize() {

        updateImage((int) audioSlider.getValue());

        int percentage = (int) audioSlider.getValue();
        audioLabel.setText(Integer.toString(percentage) + "%");

        audioSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                int percentage = (int) audioSlider.getValue();
                audioLabel.setText(Integer.toString(percentage) + "%");
                updateImage(percentage);
            }
        });

    }

    public void updateImage(int percentage) {
        String imagePath;
        if (percentage < 30) {
            imagePath = "/Images/volume_image2.png";
        } else if (percentage < 65) {
            imagePath = "/Images/volume_images3.png";
        } else {
            imagePath = "/Images/volume_image4.png";
        }

        Image image = new Image(getClass().getResourceAsStream(imagePath));
        audioImage.setImage(image);
    }
    public void changeToScreen(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/login/Login_screen.fxml"));
        Stage menuStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        menuStage.setScene(scene);
        menuStage.show();
    }



}
