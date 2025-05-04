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
    private Button restartButton;

    @FXML
    private Button menuButton;

    @FXML
    private Button closeButton;

    private GameController gameController;
    @FXML
    private VBox imageContainer;

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
            e.printStackTrace();
        }
    }


    public void setGameController(GameController controller) {
        this.gameController = controller;
    }

    public void setStats(String time, int moves) {
        statsLabel.setText("Čas: " + time + ", Počet tahů: " + moves);
    }

    @FXML
    public void onRestartGame() {
        closeDialog();
        if (gameController != null) {
            gameController.restartGame();
        }
    }

    @FXML
    public void onMainMenu() {
        closeDialog();
        if (gameController != null) {
            gameController.goToMainMenu();
        }
    }

    @FXML
    public void onClose() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}