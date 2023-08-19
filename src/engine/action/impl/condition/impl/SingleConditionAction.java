package engine.action.impl.condition.impl;

import engine.action.impl.condition.api.ConditionAction;
import engine.action.impl.condition.api.ConditionType;
import engine.definition.entity.EntityDefinition;
import engine.definition.property.api.PropertyType;
import engine.execution.context.Context;
import engine.action.api.Action;
import engine.execution.context.ContextImpl;
import engine.execution.expression.impl.ExpressionCalculator;
import engine.execution.instance.enitty.EntityInstance;

import java.util.ArrayList;

public class SingleConditionAction extends ConditionAction {

    SingleCondition singleCondition;

    public SingleConditionAction(EntityDefinition entityDefinition, SingleCondition singleCondition, ArrayList<Action> thenActions, ArrayList<Action> elseActions) {
        super(entityDefinition,thenActions, elseActions);
        this.singleCondition = singleCondition;
    }

    @Override
    public void invoke(Context context) {
        ArrayList<Action> actionsOnRun;
        if (singleCondition.calcCondition(context)) {
            actionsOnRun = new ArrayList<Action>(this.getThenActions());
        }
        else {
            actionsOnRun = new ArrayList<Action>(this.getElseActions());
        }
        for (Action action : actionsOnRun) {
            Context newContext = new ContextImpl(context.getPrimaryEntityInstance(), context.getEntityInstanceManager(), context.getActiveEnvironment());
            action.invoke(newContext);
        }
    }

    public SingleCondition getSingleCondition() {
        return singleCondition;
    }
}
