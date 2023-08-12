package engine.action.impl;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.definition.entity.EntityDefinition;
import engine.execution.context.Context;

public class SetAction extends AbstractAction {

    private final String entityName;
    private final String propertyName;
    private final String valueExpression;

    public SetAction( EntityDefinition entityDefinition, String entityName, String propertyName, String valueExpression) {
        super(ActionType.SET, entityDefinition);
        this.entityName = entityName;
        this.propertyName = propertyName;
        this.valueExpression = valueExpression;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getValueExpression() {
        return valueExpression;
    }

    @Override
    public void invoke(Context context) {

    }
}
