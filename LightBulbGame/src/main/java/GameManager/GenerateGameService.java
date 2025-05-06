/**
 * IJA Project
 * @author Tomáš Boudný (xboudn05)
 */

package GameManager;

import GameLogic.Common.*;
import GameLogic.Game.Game;

public class GenerateGameService {
    public static Game generateByDifficulty(int difficulty) {
        Game newGame = switch (difficulty) {
            case 1 -> generateEasyGame();
            case 2 -> generateMediumGame();
            case 3 -> generateHardGame();
            default -> throw new IllegalArgumentException("Unknown difficulty: " + difficulty);
        };

        newGame.updateGame();

        return newGame;
    }

    private static Game generateEasyGame() {
        Game game = Game.create(5, 5);

        // Row 1.
        game.createLinkNode(new Position(1, 1), Side.EAST, Side.SOUTH);
        game.createLinkNode(new Position(1, 2), Side.EAST, Side.WEST);
        game.createLinkNode(new Position(1, 3), Side.EAST, Side.WEST);
        game.createLinkNode(new Position(1, 4), Side.EAST, Side.WEST, Side.SOUTH);
        game.createBulbNode(new Position(1, 5), Side.WEST);

        // Row 2.
        game.createBulbNode(new Position(2, 1), Side.NORTH);
        game.createBulbNode(new Position(2, 2), Side.SOUTH);
        game.createLinkNode(new Position(2, 3), Side.EAST, Side.SOUTH);
        game.createPowerNode(new Position(2, 4), Side.WEST, Side.NORTH);
        game.createBulbNode(new Position(2, 5), Side.SOUTH);

        // Row 3.
        game.createBulbNode(new Position(3, 1), Side.SOUTH);
        game.createLinkNode(new Position(3, 2), Side.EAST, Side.NORTH, Side.SOUTH);
        game.createLinkNode(new Position(3, 3), Side.WEST, Side.NORTH);
        game.createLinkNode(new Position(3, 4), Side.EAST, Side.SOUTH);
        game.createLinkNode(new Position(3, 5), Side.WEST, Side.NORTH, Side.SOUTH);

        // Row 4.
        game.createLinkNode(new Position(4, 1), Side.EAST, Side.NORTH, Side.SOUTH);
        game.createLinkNode(new Position(4, 2), Side.EAST, Side.WEST, Side.NORTH);
        game.createBulbNode(new Position(4, 3), Side.WEST);
        game.createLinkNode(new Position(4, 4), Side.NORTH, Side.SOUTH);
        game.createBulbNode(new Position(4, 5), Side.NORTH);

        // Row 5.
        game.createLinkNode(new Position(5, 1), Side.EAST, Side.NORTH);
        game.createLinkNode(new Position(5, 2), Side.EAST, Side.WEST);
        game.createLinkNode(new Position(5, 3), Side.EAST, Side.WEST);
        game.createLinkNode(new Position(5, 4), Side.EAST, Side.WEST, Side.NORTH);
        game.createBulbNode(new Position(5, 5), Side.WEST);

        return game;
    }

    private static Game generateMediumGame() {
        Game game = Game.create(7, 7);

        // Row 1.
        game.createLinkNode(new Position(1, 1), Side.EAST, Side.SOUTH);
        game.createLinkNode(new Position(1, 2), Side.WEST, Side.SOUTH);
        game.createLinkNode(new Position(1, 3), Side.EAST, Side.SOUTH);
        game.createPowerNode(new Position(1, 4), Side.EAST, Side.WEST, Side.SOUTH);
        game.createBulbNode(new Position(1, 5), Side.WEST);
        game.createBulbNode(new Position(1, 6), Side.EAST);
        game.createLinkNode(new Position(1, 7), Side.WEST, Side.SOUTH);

        // Row 2.
        game.createBulbNode(new Position(2, 1), Side.NORTH);
        game.createLinkNode(new Position(2, 2), Side.EAST, Side.NORTH, Side.SOUTH);
        game.createLinkNode(new Position(2, 3), Side.WEST, Side.NORTH);
        game.createLinkNode(new Position(2, 4), Side.EAST, Side.NORTH, Side.SOUTH);
        game.createLinkNode(new Position(2, 5), Side.WEST, Side.SOUTH);
        game.createBulbNode(new Position(2, 6), Side.EAST);
        game.createLinkNode(new Position(2, 7), Side.WEST, Side.NORTH, Side.SOUTH);

        // Row 3.
        game.createBulbNode(new Position(3, 1), Side.EAST);
        game.createLinkNode(new Position(3, 2), Side.EAST, Side.WEST, Side.NORTH);
        game.createBulbNode(new Position(3, 3), Side.WEST);
        game.createLinkNode(new Position(3, 4), Side.NORTH, Side.SOUTH);
        game.createLinkNode(new Position(3, 5), Side.EAST, Side.NORTH, Side.SOUTH);
        game.createBulbNode(new Position(3, 6), Side.WEST);
        game.createLinkNode(new Position(3, 7), Side.NORTH, Side.SOUTH);

        // Row 4.
        game.createBulbNode(new Position(4, 1), Side.EAST);
        game.createLinkNode(new Position(4, 2), Side.WEST, Side.SOUTH);
        game.createBulbNode(new Position(4, 3), Side.SOUTH);
        game.createLinkNode(new Position(4, 4), Side.NORTH, Side.SOUTH);
        game.createLinkNode(new Position(4, 5), Side.NORTH, Side.SOUTH);
        game.createBulbNode(new Position(4, 6), Side.EAST);
        game.createLinkNode(new Position(4, 7), Side.WEST, Side.NORTH, Side.SOUTH);

        // Row 5.
        game.createBulbNode(new Position(5, 1), Side.SOUTH);
        game.createLinkNode(new Position(5, 2), Side.NORTH, Side.SOUTH);
        game.createLinkNode(new Position(5, 3), Side.EAST, Side.NORTH, Side.SOUTH);
        game.createLinkNode(new Position(5, 4), Side.WEST, Side.NORTH);
        game.createLinkNode(new Position(5, 5), Side.EAST, Side.NORTH);
        game.createLinkNode(new Position(5, 6), Side.EAST, Side.WEST);
        game.createLinkNode(new Position(5, 7), Side.WEST, Side.NORTH, Side.SOUTH);

        // Row 6.
        game.createLinkNode(new Position(6, 1), Side.NORTH, Side.SOUTH);
        game.createLinkNode(new Position(6, 2), Side.EAST, Side.NORTH, Side.SOUTH);
        game.createLinkNode(new Position(6, 3), Side.WEST, Side.NORTH);
        game.createLinkNode(new Position(6, 4), Side.EAST, Side.SOUTH);
        game.createLinkNode(new Position(6, 5), Side.WEST, Side.SOUTH);
        game.createBulbNode(new Position(6, 6), Side.EAST);
        game.createLinkNode(new Position(6, 7), Side.WEST, Side.NORTH, Side.SOUTH);

        // Row 7.
        game.createLinkNode(new Position(7, 1), Side.EAST, Side.NORTH);
        game.createLinkNode(new Position(7, 2), Side.EAST, Side.WEST, Side.NORTH);
        game.createBulbNode(new Position(7, 3), Side.WEST);
        game.createBulbNode(new Position(7, 4), Side.NORTH);
        game.createLinkNode(new Position(7, 5), Side.EAST, Side.NORTH);
        game.createLinkNode(new Position(7, 6), Side.EAST, Side.WEST);
        game.createLinkNode(new Position(7, 7), Side.WEST, Side.NORTH);

        return game;
    }

    private static Game generateHardGame() {
        Game game = Game.create(9, 9);

        // Row 1.
        game.createBulbNode(new Position(1, 1), Side.SOUTH);
        game.createBulbNode(new Position(1, 2), Side.EAST);
        game.createLinkNode(new Position(1, 3), Side.EAST, Side.WEST, Side.SOUTH);
        game.createLinkNode(new Position(1, 4), Side.EAST, Side.WEST);
        game.createLinkNode(new Position(1, 5), Side.EAST, Side.WEST);
        game.createLinkNode(new Position(1, 6), Side.EAST, Side.WEST);
        game.createLinkNode(new Position(1, 7), Side.EAST, Side.WEST);
        game.createLinkNode(new Position(1, 8), Side.EAST, Side.WEST, Side.SOUTH);
        game.createLinkNode(new Position(1, 9), Side.WEST, Side.SOUTH);

        // Row 2.
        game.createLinkNode(new Position(2, 1), Side.EAST, Side.NORTH);
        game.createLinkNode(new Position(2, 2), Side.WEST, Side.SOUTH);
        game.createLinkNode(new Position(2, 3), Side.NORTH, Side.SOUTH);
        game.createLinkNode(new Position(2, 4), Side.EAST, Side.SOUTH);
        game.createLinkNode(new Position(2, 5), Side.EAST, Side.WEST, Side.SOUTH);
        game.createLinkNode(new Position(2, 6), Side.EAST, Side.WEST);
        game.createLinkNode(new Position(2, 7), Side.WEST, Side.SOUTH);
        game.createLinkNode(new Position(2, 8), Side.NORTH, Side.SOUTH);
        game.createBulbNode(new Position(2, 9), Side.NORTH);

        // Row 3.
        game.createBulbNode(new Position(3, 1), Side.EAST);
        game.createLinkNode(new Position(3, 2), Side.WEST, Side.NORTH, Side.SOUTH);
        game.createLinkNode(new Position(3, 3), Side.EAST, Side.NORTH);
        game.createLinkNode(new Position(3, 4), Side.WEST, Side.NORTH, Side.SOUTH);
        game.createBulbNode(new Position(3, 5), Side.NORTH);
        game.createLinkNode(new Position(3, 6), Side.EAST, Side.SOUTH);
        game.createLinkNode(new Position(3, 7), Side.WEST, Side.NORTH);
        game.createLinkNode(new Position(3, 8), Side.NORTH, Side.SOUTH);
        game.createBulbNode(new Position(3, 9), Side.SOUTH);

        // Row 4.
        game.createBulbNode(new Position(4, 1), Side.EAST);
        game.createLinkNode(new Position(4, 2), Side.EAST, Side.WEST, Side.NORTH, Side.SOUTH);
        game.createLinkNode(new Position(4, 3), Side.EAST, Side.WEST);
        game.createLinkNode(new Position(4, 4), Side.WEST, Side.NORTH);
        game.createBulbNode(new Position(4, 5), Side.EAST);
        game.createLinkNode(new Position(4, 6), Side.WEST, Side.NORTH);
        game.createLinkNode(new Position(4, 7), Side.EAST, Side.SOUTH);
        game.createLinkNode(new Position(4, 8), Side.EAST, Side.WEST, Side.NORTH, Side.SOUTH);
        game.createLinkNode(new Position(4, 9), Side.WEST, Side.NORTH);

        // Row 5.
        game.createBulbNode(new Position(5, 1), Side.SOUTH);
        game.createLinkNode(new Position(5, 2), Side.EAST, Side.NORTH);
        game.createLinkNode(new Position(5, 3), Side.EAST, Side.WEST, Side.SOUTH);
        game.createLinkNode(new Position(5, 4), Side.EAST, Side.WEST);
        game.createLinkNode(new Position(5, 5), Side.WEST, Side.SOUTH);
        game.createBulbNode(new Position(5, 6), Side.SOUTH);
        game.createLinkNode(new Position(5, 7), Side.NORTH, Side.SOUTH);
        game.createBulbNode(new Position(5, 8), Side.NORTH);
        game.createBulbNode(new Position(5, 9), Side.SOUTH);

        // Row 6.
        game.createLinkNode(new Position(6, 1), Side.EAST, Side.NORTH, Side.SOUTH);
        game.createBulbNode(new Position(6, 2), Side.WEST);
        game.createBulbNode(new Position(6, 3), Side.NORTH);
        game.createLinkNode(new Position(6, 4), Side.EAST, Side.SOUTH);
        game.createLinkNode(new Position(6, 5), Side.WEST, Side.NORTH);
        game.createLinkNode(new Position(6, 6), Side.EAST, Side.NORTH, Side.SOUTH);
        game.createLinkNode(new Position(6, 7), Side.WEST, Side.NORTH);
        game.createLinkNode(new Position(6, 8), Side.EAST, Side.SOUTH);
        game.createLinkNode(new Position(6, 9), Side.WEST, Side.NORTH, Side.SOUTH);

        // Row 7.
        game.createPowerNode(new Position(7, 1), Side.EAST, Side.NORTH, Side.SOUTH);
        game.createLinkNode(new Position(7, 2), Side.WEST, Side.SOUTH);
        game.createBulbNode(new Position(7, 3), Side.EAST);
        game.createLinkNode(new Position(7, 4), Side.EAST, Side.WEST, Side.NORTH);
        game.createLinkNode(new Position(7, 5), Side.WEST, Side.SOUTH);
        game.createLinkNode(new Position(7, 6), Side.NORTH, Side.SOUTH);
        game.createLinkNode(new Position(7, 7), Side.EAST, Side.SOUTH);
        game.createLinkNode(new Position(7, 8), Side.WEST, Side.NORTH);
        game.createLinkNode(new Position(7, 9), Side.NORTH, Side.SOUTH);

        // Row 8.
        game.createLinkNode(new Position(8, 1), Side.NORTH, Side.SOUTH);
        game.createLinkNode(new Position(8, 2), Side.NORTH, Side.SOUTH);
        game.createBulbNode(new Position(8, 3), Side.SOUTH);
        game.createBulbNode(new Position(8, 4), Side.SOUTH);
        game.createLinkNode(new Position(8, 5), Side.NORTH, Side.SOUTH);
        game.createLinkNode(new Position(8, 6), Side.NORTH, Side.SOUTH);
        game.createBulbNode(new Position(8, 7), Side.NORTH);
        game.createLinkNode(new Position(8, 8), Side.EAST, Side.SOUTH);
        game.createLinkNode(new Position(8, 9), Side.WEST, Side.NORTH);

        // Row 9.
        game.createBulbNode(new Position(9, 1), Side.NORTH);
        game.createLinkNode(new Position(9, 2), Side.EAST, Side.NORTH);
        game.createLinkNode(new Position(9, 3), Side.EAST, Side.WEST, Side.NORTH);
        game.createLinkNode(new Position(9, 4), Side.EAST, Side.WEST, Side.NORTH);
        game.createLinkNode(new Position(9, 5), Side.WEST, Side.NORTH);
        game.createLinkNode(new Position(9, 6), Side.EAST, Side.NORTH);
        game.createLinkNode(new Position(9, 7), Side.EAST, Side.WEST);
        game.createLinkNode(new Position(9, 8), Side.EAST, Side.WEST, Side.NORTH);
        game.createBulbNode(new Position(9, 9), Side.WEST);

        return game;
    }
}
