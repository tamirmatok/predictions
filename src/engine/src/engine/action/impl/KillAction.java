package engine.action.impl;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.definition.entity.EntityDefinition;
import engine.execution.context.Context;

public class KillAction extends AbstractAction {

    public KillAction(EntityDefinition entityDefinition) {
        super(ActionType.KILL, entityDefinition);
    }

    @Override
    public void invoke(Context context) {
        context.removeEntity(context.getPrimaryEntityInstance());
    }

}
