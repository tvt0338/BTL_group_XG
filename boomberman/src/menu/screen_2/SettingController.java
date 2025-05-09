package menu.screen_2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import menu.music.music_menu;
import menu.music.music_map1;
import menu.music.music_map2;
import menu.music.music_map3;

import java.io.IOException;

public class SettingController {
    @FXML
    private Slider bgmSilder;
    @FXML
    private Button returnButton;

    public void returnToScreen(ActionEvent event) throws IOException {
        FXMLLoader switchToHelpScene = new FXMLLoader(getClass().getResource("/menu/Menu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(switchToHelpScene.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleBGMVolume() {
        // Điều chỉnh âm lượng cho tất cả các loại nhạc
        if (bgmSilder != null) {
            double volume = bgmSilder.getValue();
            music_menu.setVolume(volume);
            music_map1.setVolume(volume);
            music_map2.setVolume(volume);
            music_map3.setVolume(volume);
        }
    }
}
