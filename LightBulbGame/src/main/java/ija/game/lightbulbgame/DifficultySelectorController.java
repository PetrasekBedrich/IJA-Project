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


public class DifficultySelectorController {
    @FXML
    protected void onEasyButtonClick(ActionEvent event) {
        try {
            Parent gameView = FXMLLoader.load(getClass().getResource("GameView.fxml"));
            Scene gameScene = new Scene(gameView);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(gameScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onMediumButtonClick() {
        throw new NotImplementedError("Not implemented yet");
    }

    @FXML
    protected void onHardButtonClick() {
        throw new NotImplementedError("Not implemented yet");
    }

    @FXML
    protected void onBackButtonClick(ActionEvent event) {
        try {
            Parent mainView = FXMLLoader.load(getClass().getResource("MainView.fxml"));
            Scene mainScene = new Scene(mainView);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(mainScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
