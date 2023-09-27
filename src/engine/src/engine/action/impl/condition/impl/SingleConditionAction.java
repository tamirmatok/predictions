package engine.action.impl.condition.impl;

import engine.action.impl.SecondaryEntityDefinition;
import engine.action.impl.condition.api.ConditionAction;
import engine.definition.entity.EntityDefinition;
import engine.execution.context.Context;
import engine.action.api.Action;
import engine.execution.context.ContextImpl;
import engine.execution.instance.enitty.EntityInstance;

import java.util.ArrayList;

public class SingleConditionAction extends ConditionAction {

    SingleCondition singleCondition;

    public SingleConditionAction(EntityDefinition entityDefinition, SecondaryEntityDefinition secondaryEntityDefinition, SingleCondition singleCondition, ArrayList<Action> thenActions, ArrayList<Action> elseActions) {
        super(entityDefinition, secondaryEntityDefinition, thenActions, elseActions);
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
            ArrayList<EntityInstance> secondaryEntityInstances = null;
            if (getSecondaryEntityDefinition() != null) {
                secondaryEntityInstances = context.getEntityInstanceManager().getSecondaryEntityInstances(getSecondaryEntityDefinition(), context.getActiveEnvironment());
            }
            Context newContext = new ContextImpl(context.getPrimaryEntityInstance(),secondaryEntityInstances, context.getEntityInstanceManager(), context.getActiveEnvironment(), context.getGrid());
            newContext.setRootContexts(context.getRootContexts());
            action.invoke(newContext);
        }
        invokeOnSecondary(context);
    }

    public SingleCondition getSingleCondition() {
        return singleCondition;
    }
}
