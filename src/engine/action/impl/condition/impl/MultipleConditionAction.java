package engine.action.impl.condition.impl;

import engine.action.api.Action;
import engine.action.impl.condition.api.ConditionAction;
import engine.action.impl.condition.api.ConditionType;
import engine.definition.entity.EntityDefinition;
import engine.execution.context.Context;
import engine.execution.context.ContextImpl;
import engine.execution.instance.enitty.EntityInstance;

import java.util.ArrayList;

public class MultipleConditionAction extends ConditionAction {

    private final MultipleCondition multipleCondition;


    public MultipleConditionAction(EntityDefinition entityDefinition, MultipleCondition multipleCondition, ArrayList<Action> thenActions, ArrayList<Action> elseActions) {
        super(entityDefinition, thenActions, elseActions);
        this.multipleCondition = multipleCondition;
    }

    public MultipleCondition getMultipleCondition() {
        return multipleCondition;
    }

    @Override
    public void invoke(Context context) {
        ArrayList<Action> actionsOnRun;
        if (multipleCondition.calcCondition(context)) {
            actionsOnRun = new ArrayList<Action>(this.getThenActions());
        } else {
            actionsOnRun = new ArrayList<Action>(this.getElseActions());
        }
        for (Action action : actionsOnRun) {
            Context newContext = new ContextImpl(context.getPrimaryEntityInstance(), context.getEntityInstanceManager(), context.getActiveEnvironment());
            action.invoke(newContext);
        }
    }
}
