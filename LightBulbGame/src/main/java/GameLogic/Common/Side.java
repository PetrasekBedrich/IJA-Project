package GameLogic.Common;

public enum Side {
    EAST, WEST, NORTH, SOUTH;

    public Side rotateClockwise() {
        return switch (this) {
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
            case NORTH -> EAST;
        };
    }
}
