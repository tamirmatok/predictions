package engine.execution.instance.grid;

import engine.execution.instance.enitty.EntityInstance;

public class GridCell {

    EntityInstance entityInstance;
    private boolean isOccupied;


    public GridCell() {
        this.entityInstance = null;
        isOccupied = false;
    }

    public GridCell(EntityInstance entityInstance) {
        this.entityInstance = entityInstance;
        this.isOccupied = true;
    }

    public EntityInstance getEntityInstance() {
        return entityInstance;
    }

    public void setOccupied(EntityInstance entityInstance){
        this.entityInstance = entityInstance;
        this.isOccupied = true;
    }
}
