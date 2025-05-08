/**
 * IJA Project
 * @author Tomáš Boudný (xboudn05)
 * @Description Manages the lifecycle of a game instance.
 *              Handles game creation, shuffling, move execution, undo/redo,
 *              replay loading from log, and transition to live gameplay.
 */

package GameManager;

import GameLogic.Game.Game;
import GameLogic.Common.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GameManager {
    public Game game;
    public GameTrackingInfo tracking;
    private static final Random rand = new Random();
    private List<GameActionLogEntry> actionLog;
    private int currentStepIndex;

    /**
     * Constructs a new game manager and either creates a new game or loads a saved one.
     * If a new game is created, it is shuffled and logged; otherwise the last game is reconstructed from log.
     *
     * @param difficulty the difficulty level (1 = Easy, 2 = Medium, 3 = Hard)
     * @param createNewGame true to generate a new game; false to load from log file
     */
    public GameManager(int difficulty, boolean createNewGame) {
        if (createNewGame) {
            this.game = GenerateGameService.generateByDifficulty(difficulty);
            this.tracking = new GameTrackingInfo(game.rows(), game.cols());
            this.actionLog = new ArrayList<>();
            this.currentStepIndex = -1;

            try {
                GameLogger.saveInitialGameState(game);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

            while (isAnyBulbIsLight()) {
                shuffleRandomNodes(this.game);
            }
            this.tracking.saveCurrentAsInitial();
            GameLogger.endInitialState();
        }
        else {
            try {
                this.loadFromLogAndRecreateGame();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Rotates a node and returns whether all bulbs are lit after the move.
     *
     * @param pos the position of the node
     * @return true if all bulbs are lit after the move (game end)
     */
    public boolean rotateNodeAndCheckResult(Position pos) {
        rotateNode(pos, true);
        return areEveryBulbIsLight();
    }

    /**
     * Rotates a node at the given position, logs the action, and updates tracking.
     *
     * @param pos the position of the node to rotate
     * @param userClick true if the rotation was triggered by the user
     */
    private void rotateNode(Position pos, boolean userClick) {
        GameNode node = game.node(pos);
        if (node == null) {
            return;
        }

        node.turn();
        GameLogger.appendTurn(node);

        if (userClick) {
            GameActionLogEntry entry = new GameActionLogEntry(pos, node.toString());
            actionLog.add(entry);
            currentStepIndex++;
        }

        tracking.rotate(node.getPosition(), userClick);
    }

    /**
     * Randomly rotates each node on the board 0 to 3 times to shuffle the puzzle.
     *
     * @param game the game instance to shuffle
     */
    private void shuffleRandomNodes(Game game) {
        for (int r = 1; r <= game.rows(); r++) {
            for (int c = 1; c <= game.cols(); c++) {
                Position pos = new Position(r, c);
                int times = rand.nextInt(4);
                for (int i = 0; i < times; i++) {
                    rotateNode(pos, false);
                    GameLogger.addInitialGameTurn(game.node(pos));
                }
            }
        }
    }

    /**
     * Checks if all bulbs in the game are currently lit.
     *
     * @return true if every bulb is lit
     */
    private boolean areEveryBulbIsLight() {
        for (int r = 1; r <= game.rows(); r++) {
            for (int c = 1; c <= game.cols(); c++) {
                GameNode node = game.node(new Position(r, c));
                if (node != null && node.isBulb() && !node.light()) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Checks if at least one bulb in the game is currently lit.
     *
     * @return true if any bulb is lit
     */
    private boolean isAnyBulbIsLight() {
        for (int r = 1; r <= game.rows(); r++) {
            for (int c = 1; c <= game.cols(); c++) {
                GameNode node = game.node(new Position(r, c));
                if (node != null && node.isBulb() && node.light()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Switches from replay mode to live gameplay.
     * Discards all future logged actions and rewrites the log file to current state.
     */
    public void switchToLiveMode() {
        if (currentStepIndex < actionLog.size() - 1) {
            actionLog.subList(currentStepIndex + 1, actionLog.size()).clear();
        }

        GameLogger.truncateToCurrentStep(actionLog, currentStepIndex, game.rows(), game.cols());
        actionLog.clear();
        currentStepIndex = -1;
    }

    /**
     * Checks whether an undo operation is currently possible.
     *
     * @return true if undo can be performed
     */
    public boolean canUndo() {
        return currentStepIndex >= 0;
    }

    /**
     * Checks whether a redo operation is currently possible.
     *
     * @return true if redo can be performed
     */
    public boolean canRedo() {
        return currentStepIndex + 1 < actionLog.size();
    }

    /**
     * Undoes the last action by rotating the node back
     * and adjusting the tracking state accordingly.
     */
    public void undo() {
        if (currentStepIndex < 0) return;

        GameActionLogEntry entry = actionLog.get(currentStepIndex);
        Position pos = entry.getPosition();
        GameNode node = game.node(pos);

        for (int i = 0; i < 3; i++) node.turn();

        tracking.rotateUndo(pos);
        currentStepIndex--;
    }

    /**
     * Redoes the next action by rotating the node forward
     * and updating the tracking state.
     */
    public void redo() {
        if (currentStepIndex + 1 >= actionLog.size()) return;

        GameActionLogEntry entry = actionLog.get(currentStepIndex + 1);
        Position pos = entry.getPosition();
        GameNode node = game.node(pos);

        node.turn();
        tracking.rotate(pos, true);
        currentStepIndex++;
    }

    /**
     * Loads a game from a log file, reconstructs the board, and replays actions
     * that occurred after the initial state. Differentiates between setup and gameplay using
     * an "END INITIAL STATE" marker.
     *
     * @throws IOException if reading the log file fails
     */
    public void loadFromLogAndRecreateGame() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("log.txt"));
        int rows = 0, cols = 0;

        List<GameActionLogEntry> log = new ArrayList<>();

        // 1. Create Game.
        for (String line : lines) {
            if (line.startsWith("SIZE")) {
                String[] parts = line.split(" ");
                rows = Integer.parseInt(parts[1]);
                cols = Integer.parseInt(parts[2]);
                this.game = Game.create(rows, cols);
                this.tracking = new GameTrackingInfo(rows, cols);
            }
        }

        // 2. Create Nodes.
        Pattern pattern = Pattern.compile("\\{(\\w)\\[(\\d+)@(\\d+)]\\[([A-Z,]*)]}");
        for (String line : lines) {
            if (!line.startsWith("NODE")) continue;

            Matcher m = pattern.matcher(line.substring(5));
            if (m.matches()) {
                String type = m.group(1);
                int row = Integer.parseInt(m.group(2));
                int col = Integer.parseInt(m.group(3));
                String[] sideNames = m.group(4).split(",");
                Set<Side> sides = Arrays.stream(sideNames).filter(s -> !s.isEmpty()).map(Side::valueOf).collect(Collectors.toSet());

                Position pos = new Position(row, col);
                switch (type) {
                    case "L" -> game.createLinkNode(pos, sides.toArray(new Side[0]));
                    case "B" -> game.createBulbNode(pos, sides.iterator().next());
                    case "P" -> game.createPowerNode(pos, sides.toArray(new Side[0]));
                }
            }
        }

        boolean initialStateDone = false;

        // 3. Make turns.
        for (String line : lines) {
            if (line.equals("END INITIAL STATE")) {
                initialStateDone = true;
            }
            else if (!line.startsWith("TURN")) continue;

            Matcher m = pattern.matcher(line.substring(5));
            if (m.matches()) {
                int row = Integer.parseInt(m.group(2));
                int col = Integer.parseInt(m.group(3));
                Position pos = new Position(row, col);
                GameNode node = game.node(pos);

                if (initialStateDone) {
                    GameActionLogEntry entry = new GameActionLogEntry(pos, node.toString());
                    log.add(entry);
                }

                node.turn();
                tracking.rotate(pos, initialStateDone);
            }
        }

        this.actionLog = log;
        this.currentStepIndex = log.size() - 1;

        this.game.updateGame();
    }
}