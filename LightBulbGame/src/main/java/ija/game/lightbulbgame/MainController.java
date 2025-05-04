package ija.game.lightbulbgame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kotlin.NotImplementedError;

import java.io.IOException;

public class MainController {
    @FXML
    protected void onNewGameButtonClick(ActionEvent event) {
        try {
            Parent difficultyView = FXMLLoader.load(getClass().getResource("DifficultySelectorView.fxml"));
            Scene difficultyScene = new Scene(difficultyView);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(difficultyScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onLoadGameButtonClick() {
        throw new NotImplementedError("Not implemented yet");
    }

}