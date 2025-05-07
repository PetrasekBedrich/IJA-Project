/**
 * IJA Project
 * @author Tomáš Boudný (xboudn05)
 */

package GameManager;

import GameLogic.Common.*;

public class GameTrackingInfo {
    private final int rows;
    private final int cols;

    private final int[][] initialSteps;
    private final int[][] currentSteps;
    private final int[][] userClicks;
    private int totalClicks;

    /**
     * Constructs a new tracking info object for a game board of given size.
     *
     * @param rows the number of rows in the game board
     * @param cols the number of columns in the game board
     */
    public GameTrackingInfo(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.initialSteps = new int[rows][cols];
        this.currentSteps = new int[rows][cols];
        this.userClicks = new int[rows][cols];
        this.totalClicks = 0;
        setInitialSteps();
    }

    /**
     * Initializes all step and click tracking arrays to zero.
     * Called during construction or to reset the tracking state.
     */
    public void setInitialSteps() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                initialSteps[i][j] = 0;
                currentSteps[i][j] = 0;
                userClicks[i][j] = 0;
            }
        }
    }

    /**
     * Copies the current step values into the initial steps array,
     * storing the current game state as the new reference.
     */
    public void saveCurrentAsInitial() {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                initialSteps[r][c] = currentSteps[r][c];
            }
    }

    /**
     * Updates the tracking information when a node is rotated.
     * Increments user click counters if the move was triggered by the user.
     *
     * @param pos the position of the rotated node
     * @param userClick true if the rotation was caused by the user
     */
    public void rotate(Position pos, boolean userClick) {
        int r = pos.row() - 1;
        int c = pos.col() - 1;

        if (currentSteps[r][c] == 0) {
            currentSteps[r][c] = 3;
        }
        else {
            currentSteps[r][c]--;
        }

        if (userClick) {
            userClicks[r][c]++;
            totalClicks++;
        }
    }

    /**
     * Reverts a rotation in the tracking state and updates click counters.
     *
     * @param pos the position of the node to undo
     */
    public void rotateUndo(Position pos) {
        int r = pos.row() - 1;
        int c = pos.col() - 1;

        if (currentSteps[r][c] == 3) {
            currentSteps[r][c] = 0;
        }
        else {
            currentSteps[r][c]++;
        }

        userClicks[r][c]--;
        totalClicks--;
    }

    /**
     * Returns the number of steps remaining to reach the correct orientation for a node.
     *
     * @param pos the position of the node
     * @return number of steps from the current state to the goal state
     */
    public int getCurrentStep(Position pos) {
        return currentSteps[pos.row() - 1][pos.col() - 1];
    }

    /**
     * Returns the number of steps that were required after shuffling for the node.
     *
     * @param pos the position of the node
     * @return the number of steps after initial shuffle
     */
    public int getInitialStep(Position pos) {
        return initialSteps[pos.row() - 1][pos.col() - 1];
    }

    /**
     * Returns the number of times the user has clicked on the given node.
     *
     * @param pos the position of the node
     * @return number of user interactions
     */
    public int getUserClicks(Position pos) {
        return userClicks[pos.row() - 1][pos.col() - 1];
    }

    /**
     * Returns the total number of user clicks made in the game.
     *
     * @return total number of recorded user clicks
     */
    public  int getTotalClicks() {
        return totalClicks;
    }
}