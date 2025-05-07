/**
 * IJA Project
 * @author Tomáš Boudný (xboudn05)
 */

package GameManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import GameLogic.Game.Game;
import GameLogic.Common.*;

public class GameLogger {
    private static final File logFile = new File("log.txt");;
    private static final List<String> initialNodeLines = new ArrayList<>();

    /**
     * Saves the initial state of the game to the log file.
     * This includes the board size and all NODE definitions.
     *
     * @param game the game instance whose state should be saved
     * @throws IOException if writing to the log file fails
     */
    public static void saveInitialGameState(Game game) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(logFile))) {
            out.println("SIZE " + game.rows() + " " + game.cols());

            for (int r = 1; r <= game.rows(); r++) {
                for (int c = 1; c <= game.cols(); c++) {
                    GameNode node = game.node(new Position(r, c));
                    if (node != null) {
                        String nodeLine = "NODE {" + node.toString() + "}";
                        out.println(nodeLine);
                        initialNodeLines.add(nodeLine);
                    }
                }
            }
        }
    }

    /**
     * Appends a single TURN entry to the log file,
     * representing a rotation of the given node.
     *
     * @param node the node that was rotated
     */
    public static void appendTurn(GameNode node) {
        try (PrintWriter out = new PrintWriter(new FileWriter(logFile, true))) {
            out.println("TURN " + node.toString());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Truncates the log file to reflect the current game state.
     * Keeps SIZE and NODE entries, then includes only performed TURN entries
     * up to the current step index.
     *
     * @param actionLog the list of all logged actions
     * @param currentStepIndex the index of the last executed step
     * @param rows the number of rows in the game board
     * @param cols the number of columns in the game board
     */
    public static void truncateToCurrentStep(List<GameActionLogEntry> actionLog, int currentStepIndex, int rows, int cols) {
        try (PrintWriter out = new PrintWriter(new FileWriter(logFile))) {
            out.println("SIZE " + rows + " " + cols);

            for (String nodeLine : initialNodeLines) {
                out.println(nodeLine);
            }

            for (int i = 0; i <= currentStepIndex; i++) {
                out.println(actionLog.get(i).toString());
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Returns the list of initial NODE lines representing the board state
     * at the beginning of the game.
     *
     * @return list of NODE log lines
     */
    public List<String> getInitialNodeLines() {
        return initialNodeLines;
    }

    /**
     * Returns the log file object used for logging game events.
     *
     * @return the File instance of the log file
     */
    public File getLogFile() {
        return logFile;
    }
}