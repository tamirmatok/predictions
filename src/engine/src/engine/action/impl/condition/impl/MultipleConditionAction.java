package engine.action.impl.condition.impl;

import engine.action.api.AbstractAction;
import engine.action.api.Action;
import engine.action.impl.SecondaryEntityDefinition;
import engine.action.impl.condition.api.ConditionAction;
import engine.definition.entity.EntityDefinition;
import engine.execution.context.Context;
import engine.execution.context.ContextImpl;
import engine.execution.instance.enitty.EntityInstance;

import java.util.ArrayList;

public class MultipleConditionAction extends ConditionAction {

    private final MultipleCondition multipleCondition;


    public MultipleConditionAction(EntityDefinition entityDefinition, SecondaryEntityDefinition secondaryEntityDefinition, MultipleCondition multipleCondition, ArrayList<Action> thenActions, ArrayList<Action> elseActions) {
        super(entityDefinition,secondaryEntityDefinition, thenActions, elseActions);
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
            ArrayList<EntityInstance> secondaryEntityInstances = null;
            if (getSecondaryEntityDefinition() != null) {
                secondaryEntityInstances = context.getEntityInstanceManager().getSecondaryEntityInstances(getSecondaryEntityDefinition(), context.getActiveEnvironment());
            }
            Context newContext = new ContextImpl(context.getPrimaryEntityInstance(), secondaryEntityInstances, context.getEntityInstanceManager(), context.getActiveEnvironment(), context.getGrid());
            newContext.setRootContexts(context.getRootContexts());
            newContext.setCurrentTick(context.getCurrentTick());
            action.invoke(newContext);
        }
        invokeOnSecondary(context);
    }

}
