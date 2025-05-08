package ija.game.lightbulbgame;

import GameLogic.Common.GameNode;
import GameLogic.Common.GameNodeType;
import GameLogic.Common.Position;
import GameManager.GameManager;
import ija.ija2024.tool.common.Observable;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class GameController {
    @FXML
    private GridPane gameBoard;

    @FXML
    private Label timerLabel;

    @FXML
    private Label movesLabel;

    @FXML
    private Button undoButton;

    @FXML
    private Button redoButton;

    @FXML
    private Button playButton;

    private Timeline timeline;
    private int secondsElapsed = 0;
    private GameManager gameManager;
    private int difficulty;
    private boolean canPlay;

    /**
     * Sets the game difficulty and initializes the game view.
     *
     * @param difficulty the selected difficulty level
     * @param createNewGame true if a new game should be created, false to load from log
     */
    public void setDifficulty(int difficulty, boolean createNewGame) {
        this.difficulty = difficulty;
        init(createNewGame);
    }

    /**
     * Initializes the game state, UI bindings, and logic.
     * Starts the timer and prepares the game board.
     *
     * @param createNewGame true if a new game is being started
     */
    public void init(boolean createNewGame) {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
        timeline.setCycleCount(Animation.INDEFINITE);

        if (createNewGame) {
            timeline.play();
            playButton.setVisible(false);
            canPlay = true;
        }
        else {
            playButton.setVisible(true);
            canPlay = false;
        }

        gameManager = new GameManager(difficulty, createNewGame);

        registerObserverForAllNodes();
        createGameBoard();
        updateMoves();
        updateButtonStates();
    }

    /**
     * Registers a UI observer on every game node to update tiles
     * when a node's state changes.
     */
    private void registerObserverForAllNodes() {
        var uiObserver = new Observable.Observer() {
            @Override
            public void update(Observable observable) {
                GameNode node = (GameNode) observable;
                updateTile(node);
            }
        };

        int rows = gameManager.game.rows();
        int cols = gameManager.game.cols();

        for (int row = 1; row <= rows; row++) {
            for (int col = 1; col <= cols; col++) {
                GameNode node = gameManager.game.node(new Position(row, col));
                node.addObserver(uiObserver);
            }
        }
    }

    /**
     * Updates the tile's image and state based on the given game node.
     *
     * @param node the game node to reflect in the UI
     */
    private void updateTile(GameNode node) {
        Position position = node.getPosition();
        int row = position.row();
        int col = position.col();

        StackPane tile = null;
        for (Node child : gameBoard.getChildren()) {
            if (GridPane.getRowIndex(child) == row && GridPane.getColumnIndex(child) == col) {
                if (child instanceof StackPane) {
                    tile = (StackPane) child;
                    break;
                }
            }
        }

        if (tile != null) {
            tile.getChildren().removeIf(n -> n instanceof ImageView);
            var tileType = node.Type;
            String imagePath = getFile(node);
            if (imagePath != null) {
                try {
                    var wireFile = getFileForWire(node);
                    File file = new File("resources" + imagePath);
                    Image image = new Image(file.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(tileType == GameNodeType.POWER && wireFile != null ? 32 : 64);
                    imageView.setFitHeight(tileType == GameNodeType.POWER && wireFile != null ? 32 : 64);
                    imageView.setPreserveRatio(true);
                    if (tileType == GameNodeType.POWER && wireFile  != null) {
                        String backgroundImagePath = "resources" + getFileForWire(node);
                        File backgroundFile = new File(backgroundImagePath);
                        Image backgroundImage = new Image(backgroundFile.toURI().toString());
                        ImageView backgroundImageView = new ImageView(backgroundImage);
                        backgroundImageView.setFitWidth(64);
                        backgroundImageView.setFitHeight(64);
                        backgroundImageView.setPreserveRatio(true);
                        tile.getChildren().add(backgroundImageView);
                    }
                    tile.getChildren().add(imageView);
                    setCorrectRotation(tile, node);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            Label numberLabel = new Label(String.valueOf(gameManager.tracking.getCurrentStep(new Position(row,col))));
            numberLabel.getStyleClass().add("number-label");
            //Ensures the label stays hidden
            numberLabel.setMinWidth(-100);
            numberLabel.setVisible(false);
            tile.setUserData(numberLabel);
            tile.getChildren().add(numberLabel);
        }
    }

    /**
     * Creates and populates the game board grid with tiles and click handlers.
     */
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

                tile.setOnMouseClicked(event -> {
                    if (!canPlay) {
                        return;
                    }
                    handleTileClick(r, c);
                });

                gameBoard.add(tile, col, row);
            }
        }
    }

    /**
     * Creates a visual tile for a specific cell in the game board grid.
     *
     * @param row the row index
     * @param col the column index
     * @return the configured StackPane tile
     */
    private StackPane createTile(int row, int col) {
        StackPane tile = new StackPane();
        tile.getStyleClass().add("game-tile");

        var node = gameManager.game.node(new Position(row, col));
        var tileType = node.Type;

        if (tileType != GameNodeType.NONE) {
            String imagePath = getFile(node);

            if (imagePath != null) {
                try {
                    var wireFile = getFileForWire(node);
                    File file = new File("resources" + imagePath);
                    Image image = new Image(file.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(tileType == GameNodeType.POWER && wireFile != null ? 32 : 64);
                    imageView.setFitHeight(tileType == GameNodeType.POWER && wireFile != null ? 32 : 64);
                    imageView.setPreserveRatio(true);
                    if (tileType == GameNodeType.POWER && wireFile  != null) {
                        String backgroundImagePath = "resources" + getFileForWire(node);
                        File backgroundFile = new File(backgroundImagePath);
                        Image backgroundImage = new Image(backgroundFile.toURI().toString());
                        ImageView backgroundImageView = new ImageView(backgroundImage);
                        backgroundImageView.setFitWidth(64);
                        backgroundImageView.setFitHeight(64);
                        backgroundImageView.setPreserveRatio(true);
                        tile.getChildren().add(backgroundImageView);
                    }
                    tile.getChildren().add(imageView);
                    setCorrectRotation(tile, node);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }

        Label numberLabel = new Label(String.valueOf(gameManager.tracking.getCurrentStep(new Position(row,col))));
        numberLabel.getStyleClass().add("number-label");
        //Ensures the label stays hidden
        numberLabel.setMinWidth(-100);
        numberLabel.setVisible(false);
        tile.setUserData(numberLabel);
        tile.getChildren().add(numberLabel);

        return tile;
    }

    private Boolean showNumbers = false;

    /**
     * Sets the correct image rotation for the tile based on node connections.
     *
     * @param tile the tile to rotate
     * @param node the game node providing connection data
     */
    private void setCorrectRotation(StackPane tile, GameNode node) {
        ImageView imageView = null;
        for (Node child : tile.getChildren()) {
            if (child instanceof ImageView) {
                imageView = (ImageView) child;
                break;
            }
        }

        if (imageView == null) {
            return;
        }

        double rotation = 0;

        if (node.isBulb()) {
            if (node.west()) rotation = 90;
            else if (node.north()) rotation = 180;
            else if (node.east()) rotation = 270;
        }
        else if (node.isLink() || node.isPower()) {
            if (node.north() && node.east() && node.south() && node.west()) {
                rotation = 0;
            }
            else if (node.north() && node.east() && node.south() && !node.west()) {
                rotation = 0;
            }
            else if (node.east() && node.south() && node.west() && !node.north()) {
                rotation = 90;
            }
            else if (node.north() && node.south() && node.west() && !node.east()) {
                rotation = 180;
            }
            else if (node.north() && node.east() && node.west() && !node.south()) {
                rotation = 270;
            }
            else if (node.north() && node.south() && !node.east() && !node.west()) {
                rotation = 0;
            }
            else if (node.east() && node.west() && !node.north() && !node.south()) {
                rotation = 90;
            }
            else if (node.south() && node.east() && !node.north() && !node.west()) {
                rotation = 0;
            }
            else if (node.west() && node.south() && !node.north() && !node.east()) {
                rotation = 90;
            }
            else if (node.north() && node.west() && !node.south() && !node.east()) {
                rotation = 180;
            }
            else if (node.north() && node.east() && !node.south() && !node.west()) {
                rotation = 270;
            }
            else
            {
                if (node.east()) rotation = 90;
                else if (node.south()) rotation = 180;
                else if (node.west()) rotation = 270;
            }
        }

        imageView.setRotate(rotation);
    }

    /**
     * Returns the image path for a given node based on its type and state.
     *
     * @param node the game node
     * @return relative path to the image file
     */
    private String getFile(GameNode node)
    {
        if (node.Type == GameNodeType.POWER) {
            return "/Battery.png";
        }
        if (node.Type == GameNodeType.BULB) {
            if(node.light()) {
                return "/LightBulbOn.png";
            }
            else {
                return "/LightBulbOff.png";
            }
        }
        else if (node.Type == GameNodeType.LINK) {
            return getFileForWire(node);
        }
        else {
            return "";
        }
    }

    /**
     * Returns the image path for a wire based on the number and direction of connections.
     *
     * @param node the game node representing a wire
     * @return relative path to the wire image or null if invalid
     */
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

    /**
     * Handles the user clicking a tile, applies rotation, updates game state,
     * and checks for a win condition.
     *
     * @param row the row of the clicked tile
     * @param col the column of the clicked tile
     */
    private void handleTileClick(int row, int col) {
        if(showNumbers)
            return;
        GameNode node = gameManager.game.node(new Position(row, col));
        Node tileNode = null;
        for (Node child : gameBoard.getChildren()) {
            if (GridPane.getRowIndex(child) == row && GridPane.getColumnIndex(child) == col) {
                tileNode = child;
                break;
            }
        }

        if (tileNode instanceof StackPane tile) {

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

        updateMoves();

        if (solved) {
            handleGameWin();
        }
    }

    /**
     * Updates the UI label showing the number of user moves.
     */
    private void updateMoves() {
        movesLabel.setText("Tahy: " + gameManager.tracking.getTotalClicks());
    }

    /**
     * Enables or disables the undo/redo buttons based on available actions.
     */
    private void updateButtonStates() {
        undoButton.setDisable(isUndoDisable());
        redoButton.setDisable(isRedoDisable());
    }

    /**
     * Updates the timer label every second to show elapsed play time.
     */
    private void updateTimer() {
        secondsElapsed++;
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    /**
     * Shortcut method to trigger returning to the main menu.
     */
    public void goToMainMenu() {
        onBackButtonClick();
    }

    /**
     * Handles game win logic: stops timer and shows a win dialog with stats.
     */
    private void handleGameWin() {
        timeline.stop();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Modals/WinDialog.fxml"));
            Parent root = loader.load();

            WinDialogController dialogController = loader.getController();
            dialogController.setGameController(this);
            dialogController.setStats(formatTime(secondsElapsed), gameManager.tracking.getTotalClicks());

            Scene scene = new Scene(root);
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Vítězství!");
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(gameBoard.getScene().getWindow());

            dialogStage.showAndWait();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Formats a time value in seconds into MM:SS string format.
     *
     * @param seconds total elapsed time in seconds
     * @return formatted string in mm:ss format
     */
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    /**
     * Handles switching from replay mode to live gameplay when play is clicked.
     *
     * @param event the action event
     */
    @FXML
    private void onPlayButtonClick(ActionEvent event) {
        timeline.play();
        gameManager.switchToLiveMode();
        canPlay = true;
        updateButtonStates();
        playButton.setVisible(false);
    }

    /**
     * Handles returning from the game view to the main menu.
     */
    @FXML
    private void onBackButtonClick() {
        try {
            timeline.stop();
            Parent menuView = FXMLLoader.load(getClass().getResource("MainView.fxml"));
            Scene menuScene = new Scene(menuView);
            Stage window = (Stage) (gameBoard.getScene().getWindow());
            window.setScene(menuScene);
            window.show();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Performs an undo action, updates moves and UI button states.
     *
     * @param event the action event
     */
    @FXML
    private void onUndoButtonClick(ActionEvent event) {
        timeline.stop();
        gameManager.undo();
        updateMoves();
        updateButtonStates();
    }

    /**
     * Performs a redo action, updates moves and UI button states.
     *
     * @param event the action event
     */
    @FXML
    private void onRedoButtonClick(ActionEvent event) {
        gameManager.redo();
        updateMoves();
        updateButtonStates();
    }

    /**
     * Toggles display of step hints (remaining rotations) over each tile.
     *
     * @param event the action event
     */
    @FXML
    private void onHintButtonClick(ActionEvent event) {
        showNumbers = !showNumbers;

        int rows = gameManager.game.rows();
        int cols = gameManager.game.cols();

        for (int row = 0; row < rows + 1; row++) {
            for (int col = 0; col < cols + 1; col++) {
                Node node = getNodeFromGridPane(gameBoard, col, row);

                if (node instanceof StackPane tile) {
                    for (Node tileChild : tile.getChildren()) {
                        if (tileChild instanceof Label numberLabel) {
                            numberLabel.setVisible(showNumbers);
                            //Plus one for the 1 based indexing of the game
                            numberLabel.setText(gameManager.tracking.getCurrentStep(new Position(row,col)) + "");
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks whether the undo button should be disabled.
     *
     * @return true if undo is not available
     */
    private boolean isUndoDisable() {
        return !gameManager.canUndo();
    }

    /**
     * Checks whether the redo button should be disabled.
     *
     * @return true if redo is not available
     */
    private boolean isRedoDisable() {
        return !gameManager.canRedo();
    }

    /**
     * Returns the node located at the specified column and row in the given GridPane.
     *
     * @param gridPane the grid pane to search
     * @param col the column index (0-based)
     * @param row the row index (0-based)
     * @return the node at the given position, or null if none found
     */
    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }

        return null;
    }
}