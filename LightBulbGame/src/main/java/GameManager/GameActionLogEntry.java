package GameManager;

import GameLogic.Common.Position;

public class GameActionLogEntry {
    private final Position position;
    private final String nodeStateBefore; // výsledek node.toString() před otočením

    public GameActionLogEntry(Position position, String nodeStateBefore) {
        this.position = position;
        this.nodeStateBefore = nodeStateBefore;
    }

    public Position getPosition() {
        return position;
    }

    public String getNodeStateBefore() {
        return nodeStateBefore;
    }

    @Override
    public String toString() {
        return "TURN " + nodeStateBefore;
    }
}
