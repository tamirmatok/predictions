package engine.action.impl;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.definition.entity.EntityDefinition;
import engine.definition.property.api.PropertyType;
import engine.execution.context.Context;
import engine.execution.expression.impl.ExpressionCalculator;

public class SetAction extends AbstractAction {

    private final String propertyName;
    private final String valueExpression;

    public SetAction(EntityDefinition entityDefinition, String propertyName, String valueExpression) {
        super(ActionType.SET, entityDefinition);
        this.propertyName = propertyName;
        this.valueExpression = valueExpression;
    }

    @Override
    public void invoke(Context context) {
        PropertyType propertyType = context.getPrimaryEntityInstance().getPropertyByName(propertyName).getPropertyDefinition().getType();
        ExpressionCalculator expressionCalculator = new ExpressionCalculator(valueExpression, context, propertyType);
        Object value = expressionCalculator.calculate();
        switch (propertyType){
            case DECIMAL:
                Integer intVal = Integer.parseInt(value.toString());
                context.getPrimaryEntityInstance().getPropertyByName(propertyName).updateValue(intVal);
                break;
            case FLOAT:
                Float floatVal = Float.parseFloat(value.toString());
                context.getPrimaryEntityInstance().getPropertyByName(propertyName).updateValue(floatVal);
                break;
            case BOOLEAN:
                Boolean boolVal = Boolean.parseBoolean(value.toString());
                context.getPrimaryEntityInstance().getPropertyByName(propertyName).updateValue(boolVal);
                break;
            case STRING:
                String stringVal = value.toString();
                context.getPrimaryEntityInstance().getPropertyByName(propertyName).updateValue(stringVal);
                break;
        }
    }
}
