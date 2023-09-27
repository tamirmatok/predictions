package engine.action.impl;

import engine.action.api.AbstractAction;
import engine.action.api.Action;
import engine.action.api.ActionType;
import engine.definition.entity.EntityDefinition;
import engine.execution.context.Context;
import engine.execution.context.ContextImpl;
import engine.execution.expression.impl.ExpressionCalculator;
import engine.execution.instance.enitty.EntityInstance;
import engine.execution.instance.grid.Grid;
import engine.execution.instance.grid.GridCell;

import java.util.ArrayList;

public class ProximityAction extends AbstractAction {

    private final String sourceEntity;
    private final String targetEntity;
    private final String envDepthExpression;
    private final ArrayList<Action> thenActions;


    public ProximityAction(EntityDefinition entityDefinition, SecondaryEntityDefinition secondaryEntityDefinition, String targetEntity, String envDepth, ArrayList<Action> thenActions) {
        super(ActionType.PROXIMITY, entityDefinition, secondaryEntityDefinition);
        this.sourceEntity = entityDefinition.getName();
        this.targetEntity = targetEntity;
        this.envDepthExpression = envDepth;
        this.thenActions = thenActions;
    }

    @Override
    public void invoke(Context context) {
        ExpressionCalculator expressionCalculator = new ExpressionCalculator(envDepthExpression, context, null);
        // round the number to the nearest integer
        int envDepth = Math.round(Float.parseFloat(expressionCalculator.calculate().toString()));
        boolean isProximity = false;
        Grid grid = context.getGrid();
        for (int i = 0; i < grid.getNumRows(); i++) {
            for (int j = 0; j < grid.getNumCols(); j++) {
                if (grid.getCell(i, j).isOccupied() && grid.getCell(i, j).getEntityInstance().getEntityDefinition().getName().equals(sourceEntity)) {
                    // look if there is a target entity in the proximity
                    if (isProximity(i, j, targetEntity, grid, envDepth)) {
                        isProximity = true;
                        break;
                    }
                }
            }
            if (isProximity) {
                break;
            }
        }
        if (isProximity) {
            for (Action action : thenActions) {
                    ArrayList<EntityInstance> secondaryEntityInstances = null;
                    if (action.hasSecondaryEntity()) {
                        secondaryEntityInstances = context.getEntityInstanceManager().getSecondaryEntityInstances(action.getSecondaryEntityDefinition(), context.getActiveEnvironment());
                    }
                    EntityInstance entity = context.getInstanceByName(action.getMainContextEntity().getName());
                    if (entity == null){
                        throw new IllegalArgumentException("entity " + action.getMainContextEntity().getName() + " does not exist in prior context in action " + action.getActionType() + "");
                    }
                    Context newContext = new ContextImpl(entity, secondaryEntityInstances, context.getEntityInstanceManager(), context.getActiveEnvironment(), context.getGrid());
                    newContext.setRootContexts(context.getRootContexts());
                    action.invoke(newContext);
                }
            }
    }

    private boolean isProximity(int sourceRowInd, int sourceColInd, String targetEntity, Grid grid,int envDepth) {
        int gridNumRows = grid.getNumRows();
        int gridNumCols = grid.getNumCols();
        int rowInd, topLeftIndCol, squareLength;

        if ((sourceRowInd - envDepth) >= 0) {
            rowInd = sourceRowInd - envDepth;
        } else {
            rowInd = (gridNumRows) - Math.abs(sourceRowInd - envDepth);
        }
        if ((sourceColInd - envDepth) >= 0) {
            topLeftIndCol = sourceColInd - envDepth;
        } else {
            topLeftIndCol = (gridNumCols) - Math.abs(sourceColInd - envDepth);
        }

        squareLength = (3 + (envDepth - 1) * 2);

        if (grid.getNumCols() * grid.getNumRows() < squareLength * squareLength) {
            throw new IllegalArgumentException("Proximity action depth -" + envDepth + " is too large for the grid size");
        }

        for (int i = 0; i < squareLength; i++) {
            for (int j = 0; j < squareLength; j++) {
                int row = (rowInd + i) % gridNumRows;
                int col = (topLeftIndCol + j) % gridNumCols;
                GridCell gridCell = grid.getCell(row, col);
                if (gridCell.isOccupied() && gridCell.getEntityInstance().getEntityDefinition().getName().equals(targetEntity)) {
                    return true;
                }
            }
        }
        return false;
    }
}

