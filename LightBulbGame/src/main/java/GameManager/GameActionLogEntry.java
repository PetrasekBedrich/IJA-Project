/**
 * IJA Project
 * @author Tomáš Boudný (xboudn05)
 * @Description Represents a single log entry in the game action log.
 *              Stores the position and original state of a node before rotation.
 */

package GameManager;

import GameLogic.Common.Position;

public class GameActionLogEntry {
    private final Position position;
    private final String nodeStateBefore;

    /**
     * Constructs a new log entry representing a node rotation.
     *
     * @param position the position of the rotated node
     * @param nodeStateBefore the string representation of the node before the rotation
     */
    public GameActionLogEntry(Position position, String nodeStateBefore) {
        this.position = position;
        this.nodeStateBefore = nodeStateBefore;
    }

    /**
     * Returns the position of the node that was rotated.
     *
     * @return the position of the affected node
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Returns the log representation of the rotation as it appears in the log file.
     *
     * @return a formatted TURN log line
     */
    @Override
    public String toString() {
        return "TURN " + nodeStateBefore;
    }
}
