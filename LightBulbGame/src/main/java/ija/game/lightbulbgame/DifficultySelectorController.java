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
    /**
     * Handles the click event for the Easy difficulty button.
     * Loads the game view with Easy difficulty.
     *
     * @param event the triggered action event
     */
    @FXML
    protected void onEasyButtonClick(ActionEvent event) {
        loadGameViewWithDifficulty(1, event);
    }

    /**
     * Handles the click event for the Medium difficulty button.
     * Loads the game view with Medium difficulty.
     *
     * @param event the triggered action event
     */
    @FXML
    protected void onMediumButtonClick(ActionEvent event) {
        loadGameViewWithDifficulty(2, event);
    }

    /**
     * Handles the click event for the Hard difficulty button.
     * Loads the game view with Hard difficulty.
     *
     * @param event the triggered action event
     */
    @FXML
    protected void onHardButtonClick(ActionEvent event) {
        loadGameViewWithDifficulty(3, event);
    }

    /**
     * Handles the click event for the Back button.
     * Returns the user to the main menu view.
     *
     * @param event the triggered action event
     */
    @FXML
    protected void onBackButtonClick(ActionEvent event) {
        try {
            Parent mainView = FXMLLoader.load(getClass().getResource("MainView.fxml"));
            Scene mainScene = new Scene(mainView);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(mainScene);
            window.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Loads the game view (`GameView.fxml`) and sets the selected difficulty.
     *
     * @param difficulty the selected difficulty level
     * @param event the triggered action event used to get the current stage
     */
    private void loadGameViewWithDifficulty(int difficulty, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameView.fxml"));
            Parent root = loader.load();

            GameController controller = loader.getController();
            controller.setDifficulty(difficulty, true);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
