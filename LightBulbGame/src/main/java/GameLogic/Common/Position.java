package GameLogic.Common;

public record Position(int row, int col) {

    public int getCol() {
        return col;
    }

    public int getRow() {
        return this.row;
    }
}
