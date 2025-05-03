/**
 * IJA Project
 * @author Tomáš Boudný (xboudn05)
 */

package GameManager;

import GameLogic.Game.Game;
import GameLogic.Common.*;
import java.util.Random;

public class GameManager {
    public Game game;
    public GameTrackingInfo tracking;
    private static final Random rand = new Random();

    public GameManager(int difficulty) {
        this.game = GenerateGameService.generateByDifficulty(difficulty);
        this.tracking = new GameTrackingInfo(game.rows(), game.cols());

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
}