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
     * Uloží počáteční stav hry: SIZE + NODE řádky.
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
     * Přidá jeden záznam TURN do log souboru (append).
     */
    public static void appendTurn(GameNode node) {
        try (PrintWriter out = new PrintWriter(new FileWriter(logFile, true))) {
            out.println("TURN " + node.toString());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Přepíše log: ponechá SIZE + NODE, zapíše pouze provedené tahy.
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

    public List<String> getInitialNodeLines() {
        return initialNodeLines;
    }

    public File getLogFile() {
        return logFile;
    }
}