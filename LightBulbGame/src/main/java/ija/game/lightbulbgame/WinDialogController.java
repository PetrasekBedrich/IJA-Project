/**
 * IJA Project
 * @author Bedřich Petrášek (xpetra31)
 * @Description Controller for the win dialog that appears when the player finishes the game.
 *              Displays game statistics, allows return to the main menu or closing the dialog.
 */

package ija.game.lightbulbgame;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;

public class WinDialogController {
    @FXML
    private Label statsLabel;

    @FXML
    private Button closeButton;

    @FXML
    private VBox imageContainer;

    private GameController gameController;

    /**
     * Initializes the win dialog by loading and displaying a bulb image.
     * Called automatically after FXML loading.
     */
    @FXML
    public void initialize() {
        try {
            File file = new File("resources/LightBulbOn.png");
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(64);
            imageView.setFitWidth(64);
            imageView.setPreserveRatio(true);

            if (imageContainer != null) {
                imageContainer.getChildren().add(0, imageView);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Injects the reference to the parent game controller.
     * Used to return to the main menu if requested.
     *
     * @param controller the game controller to return to
     */
    public void setGameController(GameController controller) {
        this.gameController = controller;
    }

    /**
     * Sets the text displaying the game completion statistics.
     *
     * @param time the formatted elapsed time
     * @param moves the number of moves taken by the player
     */
    public void setStats(String time, int moves) {
        statsLabel.setText("Čas: " + time + ", Počet tahů: " + moves);
    }

    /**
     * Handles the click on the "Main Menu" button.
     * Closes the dialog and switches back to the main menu.
     */
    @FXML
    public void onMainMenu() {
        closeDialog();
        if (gameController != null) {
            gameController.goToMainMenu();
        }
    }

    /**
     * Handles the click on the "Close" button.
     * Closes the win dialog window.
     */
    @FXML
    public void onClose() {
        closeDialog();
    }

    /**
     * Closes the currently displayed dialog window.
     */
    private void closeDialog() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}