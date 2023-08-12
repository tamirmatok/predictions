package engine.action.impl.condition.impl;

import engine.action.impl.condition.api.ConditionAction;
import engine.action.impl.condition.api.ConditionType;
import engine.definition.entity.EntityDefinition;
import engine.execution.context.Context;
import engine.action.api.Action;
import java.util.ArrayList;

public class SingleConditionAction extends ConditionAction {

    SingleCondition singleCondition;

    public SingleConditionAction(EntityDefinition entityDefinition, SingleCondition singleCondition, ArrayList<Action> thenActions, ArrayList<Action> elseActions) {
        super(entityDefinition,thenActions, elseActions);
        this.singleCondition = singleCondition;
    }

    @Override
    public void invoke(Context context) {
    }

    public SingleCondition getSingleCondition() {
        return singleCondition;
    }
}
