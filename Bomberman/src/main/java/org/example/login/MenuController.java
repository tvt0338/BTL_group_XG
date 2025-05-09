package org.example.login;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main.module.map.Main;

import java.io.IOException;

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

    public void onPlayButtonClick(ActionEvent event) {
        System.out.println("Play button clicked");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Main mainApp = new Main();
        mainApp.start(stage);
        // Add your play game logic here
    }

    public void onHelpButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/example/Help/Help_screen.fxml"));
        Stage helpStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        helpStage.setScene(scene);
        helpStage.show();
        // Add your help screen logic here
    }

    public void onExitButtonClick(ActionEvent event) {
        System.out.println("Exiting game...");
        Platform.exit();
    }

    public void onSettingButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/example/setting/Setting_screen.fxml"));
        Stage settingStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        settingStage.setScene(scene);
        settingStage.show();

        // Add your settings screen logic here
    }


    public void onAuthorsButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/example/authors/Author_screen.fxml"));
        Stage authorStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        authorStage.setScene(scene);
        authorStage.show();
        // Add your authors screen logic here
    }
}
