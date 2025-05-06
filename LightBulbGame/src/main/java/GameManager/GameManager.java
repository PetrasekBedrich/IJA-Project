/**
 * IJA Project
 * @author Tomáš Boudný (xboudn05)
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

    public GameManager(int difficulty) {
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
    }

    public boolean rotateNodeAndCheckResult(Position pos) {
        rotateNode(pos, true);
        return areEveryBulbIsLight();
    }

    private void rotateNode(Position pos, boolean userClick) {
        GameNode node = game.node(pos);
        if (node == null) {
            return;
        }
        System.out.println("Rotating node " + pos + " into " + node);
        node.turn();

        GameLogger.appendTurn(node);

        tracking.rotate(node.getPosition(), userClick);
    }

    private void shuffleRandomNodes(Game game) {
        for (int r = 1; r <= game.rows(); r++) {
            for (int c = 1; c <= game.cols(); c++) {
                Position pos = new Position(r, c);
                int times = rand.nextInt(4);
                for (int i = 0; i < times; i++) {
                    rotateNode(pos, false);
                }
            }
        }
    }

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

    public void switchToLiveMode(String logFilePath) {
        if (currentStepIndex < actionLog.size() - 1) {
            actionLog.subList(currentStepIndex + 1, actionLog.size()).clear();
        }

        GameLogger.truncateToCurrentStep(actionLog, currentStepIndex, game.rows(), game.cols());
    }

    public boolean canUndo() {
        return currentStepIndex >= 0;
    }

    public boolean canRedo() {
        return currentStepIndex + 1 < actionLog.size();
    }

    public void undo() {
        if (currentStepIndex < 0) return;

        GameActionLogEntry entry = actionLog.get(currentStepIndex);
        Position pos = entry.getPosition();
        GameNode node = game.node(pos);

        for (int i = 0; i < 3; i++) node.turn();

        tracking.rotateUndo(pos);
        currentStepIndex--;
    }

    public void redo() {
        if (currentStepIndex + 1 >= actionLog.size()) return;

        GameActionLogEntry entry = actionLog.get(currentStepIndex + 1);
        Position pos = entry.getPosition();
        GameNode node = game.node(pos);

        node.turn();
        tracking.rotate(pos, true);
        currentStepIndex++;
    }

    public void loadFromLogAndRecreateGame(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filename));
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

        // 3. Make turns.
        for (String line : lines) {
            if (!line.startsWith("TURN")) continue;

            Matcher m = pattern.matcher(line.substring(5));
            if (m.matches()) {
                int row = Integer.parseInt(m.group(2));
                int col = Integer.parseInt(m.group(3));
                Position pos = new Position(row, col);
                GameNode node = game.node(pos);

                GameActionLogEntry entry = new GameActionLogEntry(pos, node.toString());
                log.add(entry);

                node.turn();
                tracking.rotate(pos, false);
            }
        }

        this.actionLog = log;
        this.currentStepIndex = log.size() - 1;

        this.game.updateGame();
    }
}