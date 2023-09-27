package engine.execution.instance.grid;

import engine.definition.grid.GridDefinition;
import engine.execution.instance.enitty.EntityInstance;

import java.util.ArrayList;
import java.util.Random;

public class Grid {

    GridDefinition gridDefinition;
    ArrayList<ArrayList<GridCell>> grid;
    int countOccupiedCells = 0;

    public Grid(GridDefinition gridDefinition) {
        this.grid = new ArrayList<>();
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

    public void setRandom(EntityInstance entityInstance) {
        if (countOccupiedCells == gridDefinition.getNumRows() * gridDefinition.getNumCols()) {
            throw new IllegalArgumentException("Can't insert entity" + entityInstance.getEntityDefinition().getName() + " - grid is full");
        }
        // choose random cell\
        // TODO: check about the -1 and the random
        int row = (int) (Math.random() * gridDefinition.getNumRows() - 1);
        int col = (int) (Math.random() * gridDefinition.getNumCols() - 1);
        GridCell gridCell = grid.get(row).get(col);
        if (!gridCell.isOccupied()) {
            gridCell.setOccupied(entityInstance);
            countOccupiedCells++;
        } else {
            // random failed so we will choose the first empty cell
            for (int i = 0; i < gridDefinition.getNumRows(); i++) {
                for (int j = 0; j < gridDefinition.getNumCols(); j++) {
                    gridCell = grid.get(i).get(j);
                    if (!gridCell.isOccupied()) {
                        gridCell.setOccupied(entityInstance);
                        countOccupiedCells++;
                        return;
                    }
                }
            }
        }
    }

    public void moveEntities() {
        for (int i = 0; i < gridDefinition.getNumRows(); i++) {
            for (int j = 0; j < gridDefinition.getNumCols(); j++) {
                GridCell gridCell = grid.get(i).get(j);
                if (gridCell.isOccupied()) {
                    // choose random direction
                    int randomDirection = (int) (Math.random() * 4);
                    boolean isMoveSuccessful = this.moveFromCell(i, j, randomDirection);
                    if (!isMoveSuccessful) {
                        // try to move in other directions
                        for (int k = 0; k < 4; k++) {
                            if (k != randomDirection) {
                                isMoveSuccessful = this.moveFromCell(i, j, k);
                                if (isMoveSuccessful) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public static Direction getRandomDirection() {
        Random random = new Random();
        return Direction.values()[random.nextInt(Direction.values().length)];
    }


    private boolean moveFromCell(int rowInd, int colInd, int direction) {
        boolean isMoveSuccessful = false;
        GridCell newGridCell = null;
        GridCell gridCell = grid.get(rowInd).get(colInd);
        switch (direction) {
            case 0:
                // move up
                if (rowInd == 0) {
                    newGridCell = grid.get(gridDefinition.getNumRows() - 1).get(colInd);
                } else {
                    newGridCell = grid.get(rowInd - 1).get(colInd);
                }
                break;

            case 1:
                // move down
                if (rowInd == gridDefinition.getNumRows() - 1) {
                    newGridCell = grid.get(0).get(colInd);
                } else {
                    newGridCell = grid.get(rowInd + 1).get(colInd);
                }
                break;
            case 2:
                // move left
                if (colInd == 0) {
                    newGridCell = grid.get(rowInd).get(gridDefinition.getNumCols() - 1);
                } else {
                    newGridCell = grid.get(rowInd).get(colInd - 1);
                }
                break;
            case 3:
                // move right
                if (colInd == gridDefinition.getNumCols() - 1) {
                    newGridCell = grid.get(rowInd).get(0);
                } else {
                    newGridCell = grid.get(rowInd).get(colInd + 1);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid direction");
        }
        if (!newGridCell.isOccupied()) {
            newGridCell.setOccupied(gridCell.getEntityInstance());
            gridCell.setAvailable();
            isMoveSuccessful = true;
        }
        return isMoveSuccessful;
    }

    public int getNumRows(){
        return gridDefinition.getNumRows();
    }

    public int getNumCols(){
        return gridDefinition.getNumCols();
    }


}
