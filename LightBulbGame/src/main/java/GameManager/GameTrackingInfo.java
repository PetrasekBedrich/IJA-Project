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

    public GameTrackingInfo(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.initialSteps = new int[rows][cols];
        this.currentSteps = new int[rows][cols];
        this.userClicks = new int[rows][cols];
        this.totalClicks = 0;
        setInitialSteps();
    }

    public void setInitialSteps() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                initialSteps[i][j] = 0;
                currentSteps[i][j] = 0;
                userClicks[i][j] = 0;
            }
        }
    }

    public void saveCurrentAsInitial() {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                initialSteps[r][c] = currentSteps[r][c];
            }
    }

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

    public int getCurrentStep(Position pos) {
        return currentSteps[pos.row() - 1][pos.col() - 1];
    }

    public int getInitialStep(Position pos) {
        return initialSteps[pos.row() - 1][pos.col() - 1];
    }

    public int getUserClicks(Position pos) {
        return userClicks[pos.row() - 1][pos.col() - 1];
    }

    public  int getTotalClicks() {
        return totalClicks;
    }
}