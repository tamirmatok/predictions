package engine.action.impl.condition.impl;

import engine.action.api.Action;
import engine.action.impl.condition.api.ConditionAction;
import engine.action.impl.condition.api.ConditionType;
import engine.definition.entity.EntityDefinition;
import engine.execution.context.Context;

import java.util.ArrayList;

public class MultipleConditionAction extends ConditionAction {

    private final MultipleCondition multipleCondition;


    public MultipleConditionAction(EntityDefinition entityDefinition, MultipleCondition multipleCondition, ArrayList<Action> thenActions, ArrayList<Action> elseActions) {
        super(entityDefinition,thenActions, elseActions);
        this.multipleCondition = multipleCondition;
    }

    public MultipleCondition getMultipleCondition() {
        return multipleCondition;
    }
    @Override
    public void invoke(Context context) {

    }
}
