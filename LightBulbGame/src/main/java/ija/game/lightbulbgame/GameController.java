package ija.game.lightbulbgame;

import GameLogic.Common.GameNode;
import GameLogic.Common.GameNodeType;
import GameLogic.Common.Position;
import GameManager.GameManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import kotlin.NotImplementedError;

import java.io.File;
import java.io.IOException;

public class GameController {

    @FXML
    private GridPane gameBoard;

    @FXML
    private Label timerLabel;

    @FXML
    private Label movesLabel;

    private Timeline timeline;
    private int secondsElapsed = 0;
    private int movesCount = 0;

    private GameManager gameManager;

    @FXML
    public void initialize() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // TODO: Make this dynamic
        // Probably some singleton store?
        gameManager = new GameManager(1);
        createGameBoard();
    }

    private void createGameBoard() {
        gameBoard.getChildren().clear();
        gameBoard.getRowConstraints().clear();
        gameBoard.getColumnConstraints().clear();

        int rows = gameManager.game.rows();
        int cols = gameManager.game.cols();

        gameBoard.setHgap(2);
        gameBoard.setVgap(2);

        for (int row = 1; row <= rows; row++) {
            for (int col = 1; col <= cols; col++) {
                final int r = row;
                final int c = col;

                StackPane tile = createTile(row, col);

                tile.setOnMouseClicked(event -> handleTileClick(r, c));

                gameBoard.add(tile, col, row);
            }
        }
    }

    private StackPane createTile(int row, int col) {
        StackPane tile = new StackPane();
        tile.getStyleClass().add("game-tile");

        var node = gameManager.game.node(new Position(row, col));
        var tileType = node.Type;

        if (tileType != GameNodeType.NONE) {
            String imagePath = getFile(node);

            if (imagePath != null) {
                try {
                    File file = new File("resources" + imagePath);
                    Image image = new Image(file.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(64);
                    imageView.setFitHeight(64);
                    imageView.setPreserveRatio(true);
                    tile.getChildren().add(imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return tile;
    }
    private String getFile(GameNode node)
    {
        if(node.Type == GameNodeType.NONE)
            System.out.println("NONE");
        if(node.Type == GameNodeType.POWER)
            return "/Battery.png";
        if(node.Type == GameNodeType.BULB)
        {
            if(node.light())
            {
                return "/LightBulbOn.png";
            }
            else
            {
                return "/LightBulbOff.png";
            }
        }
        else if(node.Type == GameNodeType.LINK)
        {
            return getFileForWire(node);
        }
        else
        {
            return "";
        }
    }
    private String getFileForWire(GameNode node)
    {
        int connectionCount = 0;
        if (node.north()) connectionCount++;
        if (node.south()) connectionCount++;
        if (node.east()) connectionCount++;
        if (node.west()) connectionCount++;

        if (connectionCount == 4) {
            return "/WireX.png";
        } else if (connectionCount == 3) {
            return "/Wire3.png";
        } else if (connectionCount == 2) {
            if ((node.north() && node.south()) || (node.east() && node.west())) {
                return "/WireI.png";
            } else {
                return "/WireL.png";
            }
        } else {
            return null;
        }

    }
    private void handleTileClick(int row, int col) {
        GameNode node = gameManager.game.node(new Position(row, col));

        Node tileNode = null;
        for (Node child : gameBoard.getChildren()) {
            if (GridPane.getRowIndex(child) == row && GridPane.getColumnIndex(child) == col) {
                tileNode = child;
                break;
            }
        }

        if (tileNode instanceof StackPane) {
            StackPane tile = (StackPane) tileNode;

            ImageView wireImageView = null;
            for (Node child : tile.getChildren()) {
                if (child instanceof ImageView) {
                    wireImageView = (ImageView) child;
                    break;
                }
            }

            if (wireImageView != null) {
                wireImageView.setRotate(wireImageView.getRotate() + 90);

                if (wireImageView.getRotate() >= 360) {
                    wireImageView.setRotate(0);
                }
            }
        }

        boolean solved = gameManager.rotateNodeAndCheckResult(new Position(row, col));

        movesCount++;
        movesLabel.setText("Tahy: " + movesCount);

        if (solved) {
            handleGameWin();
        }
    }


    private void updateTimer() {
        secondsElapsed++;
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void handleGameWin() {
        timeline.stop();
        // TODO: Implementovat dialog výhry nebo přechod na obrazovku výhry
    }

    @FXML
    private void onBackButtonClick(ActionEvent event) {
        try {
            // Zastavit časovač
            timeline.stop();

            // Přejít zpět na hlavní menu
            Parent menuView = FXMLLoader.load(getClass().getResource("MainView.fxml"));
            Scene menuScene = new Scene(menuView);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(menuScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onRestartButtonClick(ActionEvent event) {
        // TODO: Reconsider if game restaring is neccessary
        throw new NotImplementedError("Restart not implemented yet");

//        movesCount = 0;
//        secondsElapsed = 0;
//        movesLabel.setText("Tahy: 0");
//        timerLabel.setText("00:00");
//
//        createGameBoard();
//
//        timeline.playFromStart();
    }

    @FXML
    private void onHintButtonClick(ActionEvent event) {
        //TODO: Implement showing remaining moves to done
        throw new NotImplementedError("Not implemented yet");
    }
}