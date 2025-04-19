package menu.screen_2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;


public class HelpController {

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