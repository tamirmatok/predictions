package engine.action.impl;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.definition.entity.EntityDefinition;
import engine.execution.context.Context;

public class KillAction extends AbstractAction {

    public KillAction(EntityDefinition mainEntityDefinition) {
        super(ActionType.KILL, mainEntityDefinition);
    }

    public KillAction(EntityDefinition mainEntityDefinition, EntityDefinition secondaryEntityDefinition) {
        super(ActionType.KILL, mainEntityDefinition, secondaryEntityDefinition);
    }

    @Override
    public void invoke(Context context) {
        context.removeEntity(context.getPrimaryEntityInstance());
    }

}
