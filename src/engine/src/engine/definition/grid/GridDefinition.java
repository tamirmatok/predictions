package engine.definition.grid;

public class GridDefinition {
    private int numRows;
    private int numCols;

    public GridDefinition(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
    }

    public int getNumRows() {
        return this.numRows;
    }

    public int getNumCols() {
        return this.numCols;
    }
}
