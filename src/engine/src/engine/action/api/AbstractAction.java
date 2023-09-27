package engine.action.api;

import engine.action.impl.SecondaryEntityDefinition;
import engine.definition.entity.EntityDefinition;
import engine.execution.context.Context;
import engine.execution.context.ContextImpl;
import engine.execution.instance.enitty.EntityInstance;

import java.util.ArrayList;

public abstract class AbstractAction implements Action {

    private final ActionType actionType;
    private final EntityDefinition mainEntityDefinition;
    private final SecondaryEntityDefinition secondaryEntityDefinition;


    protected AbstractAction(ActionType actionType, EntityDefinition entityDefinition) {
        this.actionType = actionType;
        this.mainEntityDefinition = entityDefinition;
        this.secondaryEntityDefinition = null;
    }

    protected AbstractAction(ActionType actionType, EntityDefinition entityDefinition, SecondaryEntityDefinition secondaryEntityDefinition) {
        this.actionType = actionType;
        this.mainEntityDefinition = entityDefinition;
        this.secondaryEntityDefinition = secondaryEntityDefinition;
    }

    public void invokeOnSecondary(Context context){
        for (EntityInstance secondaryEntityInstance : context.getSecondaryEntityInstances()) {
            Context newContext = new ContextImpl(secondaryEntityInstance, null, context.getEntityInstanceManager(), context.getActiveEnvironment(), context.getGrid());
            newContext.setCurrentTick(context.getCurrentTick());
            newContext.setRootContexts(context.getRootContexts());
            this.invoke(newContext);
        }
    }

    @Override
    public boolean hasSecondaryEntity() {
        return secondaryEntityDefinition != null;
    }

    @Override
    public ActionType getActionType() {
        return actionType;
    }

    @Override
    public EntityDefinition getMainContextEntity() {
        return mainEntityDefinition;
    }

    public SecondaryEntityDefinition getSecondaryEntityDefinition() {
        return secondaryEntityDefinition;
    }
}
