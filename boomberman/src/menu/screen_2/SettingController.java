package menu.screen_2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SettingController {

    @FXML
    private Button returnButton;

    public void returnToScreen(ActionEvent event) throws IOException {
        FXMLLoader switchToHelpScene = new FXMLLoader(getClass().getResource("/menu/Menu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(switchToHelpScene.load());
        stage.setScene(scene);
        stage.show();
    }

}
