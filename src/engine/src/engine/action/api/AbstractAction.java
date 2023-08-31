package engine.action.api;

import engine.definition.entity.EntityDefinition;

public abstract class AbstractAction implements Action {

    private final ActionType actionType;
    private final EntityDefinition mainEntityDefinition;
    private final EntityDefinition secondaryEntityDefinition;



    protected AbstractAction(ActionType actionType, EntityDefinition entityDefinition) {
        this.actionType = actionType;
        this.mainEntityDefinition = entityDefinition;
        this.secondaryEntityDefinition = null;
    }

    protected AbstractAction(ActionType actionType, EntityDefinition entityDefinition, EntityDefinition secondaryEntityDefinition) {
        this.actionType = actionType;
        this.mainEntityDefinition = entityDefinition;
        this.secondaryEntityDefinition = secondaryEntityDefinition;
    }


    @Override
    public ActionType getActionType() {
        return actionType;
    }

    @Override
    public EntityDefinition getMainContextEntity() {
        return mainEntityDefinition;
    }

    public EntityDefinition getSecondaryEntityDefinition() {
        return secondaryEntityDefinition;
    }
}
