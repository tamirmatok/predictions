package engine.action.api;

import engine.action.impl.SecondaryEntityDefinition;
import engine.definition.entity.EntityDefinition;
import engine.execution.context.Context;

public interface Action {
    void invoke(Context context);
    ActionType getActionType();
    EntityDefinition getMainContextEntity();
    SecondaryEntityDefinition getSecondaryEntityDefinition();
    boolean hasSecondaryEntity();
}
