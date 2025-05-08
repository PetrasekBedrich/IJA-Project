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
                        String nodeLine = "NODE " + node.toString();
                        out.println(nodeLine);
                        initialNodeLines.add(nodeLine);
                    }
                }
            }
        }
    }

    /**
     * Adds a TURN entry to the in-memory initial log list.
     * Used before the actual game starts, to record rotations done during generation/shuffling.
     *
     * @param node the game node that was rotated before gameplay started
     */
    public static void addInitialGameTurn(GameNode node) {
        initialNodeLines.add("TURN " + node.toString());
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
     * Appends a marker line to the log file indicating the end of the initial game setup section.
     * This separates node definitions from user actions in the log.
     */
    public static void endInitialState() {
        try (PrintWriter out = new PrintWriter(new FileWriter(logFile, true))) {
            out.println("END INITIAL STATE");
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

            out.println("END INITIAL STATE");

            for (int i = 0; i <= currentStepIndex; i++) {
                out.println(actionLog.get(i).toString());
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}