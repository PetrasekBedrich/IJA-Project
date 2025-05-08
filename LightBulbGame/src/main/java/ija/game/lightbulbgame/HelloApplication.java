/**
 * IJA Project
 * @author Bedřich Petrášek (xpetra31)
 * @Description Entry point of the Light Bulb Game JavaFX application.
 *              Launches the JavaFX runtime and displays the main menu screen.
 */

package ija.game.lightbulbgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    /**
     * Initializes and displays the primary stage of the application.
     * Loads the main menu view from the FXML file.
     *
     * @param stage the primary stage provided by the JavaFX runtime
     * @throws IOException if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 800);
        stage.setResizable(false);
        stage.setTitle("Light Bulb Game!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main entry point of the application.
     * Launches the JavaFX application lifecycle.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}