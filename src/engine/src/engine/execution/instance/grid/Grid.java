package engine.execution.instance.grid;

import engine.definition.grid.GridDefinition;

import java.util.ArrayList;

public class Grid {

    GridDefinition gridDefinition;
    ArrayList<ArrayList<GridCell>> grid;

    public Grid(GridDefinition gridDefinition) {
        this.gridDefinition = gridDefinition;
        this.setGrid();
    }

    private void setGrid() {
        for (int i = 0; i < gridDefinition.getNumRows(); i++) {
            grid.add(new ArrayList<>(gridDefinition.getNumCols()));
            for (int j = 0; j < gridDefinition.getNumCols(); j++) {
                grid.get(i).add(new GridCell()); // Fill with initial values
            }
        }
    }

    public GridCell getCell(int row, int col) {
        return grid.get(row).get(col);
    }
}
