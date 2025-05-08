/**
 * IJA Project
 * @author Bedřich Petrášek (xpetra31)
 * @Description Controller for the main menu screen.
 *              Provides actions for starting a new game or loading a previously saved game.
 */

package ija.game.lightbulbgame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    /**
     * Handles the click event for the "New Game" button.
     * Opens the difficulty selection view.
     *
     * @param event the triggered action event
     */
    @FXML
    protected void onNewGameButtonClick(ActionEvent event) {
        try {
            Parent difficultyView = FXMLLoader.load(getClass().getResource("DifficultySelectorView.fxml"));
            Scene difficultyScene = new Scene(difficultyView);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(difficultyScene);
            window.show();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Handles the click event for the "Load Game" button.
     * Loads the saved game from the log file and opens the game view.
     *
     * @param event the triggered action event
     */
    @FXML
    protected void onLoadGameButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameView.fxml"));
            Parent root = loader.load();

            GameController controller = loader.getController();
            controller.setDifficulty(1, false);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}