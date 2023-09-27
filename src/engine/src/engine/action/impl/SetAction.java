package engine.action.impl;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.definition.entity.EntityDefinition;
import engine.definition.property.api.PropertyType;
import engine.execution.context.Context;
import engine.execution.context.ContextImpl;
import engine.execution.expression.impl.ExpressionCalculator;
import engine.execution.instance.enitty.EntityInstance;

public class SetAction extends AbstractAction {

    private final String propertyName;
    private final String valueExpression;

    public SetAction(EntityDefinition entityDefinition, String propertyName, String valueExpression) {
        super(ActionType.SET, entityDefinition);
        this.propertyName = propertyName;
        this.valueExpression = valueExpression;
    }

    public SetAction(EntityDefinition entityDefinition, SecondaryEntityDefinition secondaryEntityDefinition, String propertyName, String valueExpression) {
        super(ActionType.SET, entityDefinition, secondaryEntityDefinition);
        this.propertyName = propertyName;
        this.valueExpression = valueExpression;
    }

    @Override
    public void invoke(Context context) {
        PropertyType propertyType = context.getPrimaryEntityInstance().getPropertyByName(propertyName).getPropertyDefinition().getType();
        try{
        ExpressionCalculator expressionCalculator = new ExpressionCalculator(valueExpression, context, propertyType);
        Object value = expressionCalculator.calculate();
            switch (propertyType) {
                case DECIMAL:
                    Integer intVal = Integer.parseInt(value.toString());
                    context.getPrimaryEntityInstance().getPropertyByName(propertyName).updateValue(intVal, context.getCurrentTick());
                    break;
                case FLOAT:
                    Float floatVal = Float.parseFloat(value.toString());
                    context.getPrimaryEntityInstance().getPropertyByName(propertyName).updateValue(floatVal, context.getCurrentTick());
                    break;
                case BOOLEAN:
                    Boolean boolVal = Boolean.parseBoolean(value.toString());
                    context.getPrimaryEntityInstance().getPropertyByName(propertyName).updateValue(boolVal, context.getCurrentTick());
                    break;
                case STRING:
                    String stringVal = value.toString();
                    context.getPrimaryEntityInstance().getPropertyByName(propertyName).updateValue(stringVal, context.getCurrentTick());
                    break;
            }
            invokeOnSecondary(context);
        }
        catch (Exception e){
            throw new IllegalArgumentException("Failed execute set action - " + valueExpression + " is not of type " + propertyType.toString());
        }
    }
}
