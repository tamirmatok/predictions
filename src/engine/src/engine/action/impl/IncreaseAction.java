package engine.action.impl;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.definition.entity.EntityDefinition;
import engine.definition.property.api.PropertyType;
import engine.execution.context.Context;
import engine.execution.context.ContextImpl;
import engine.execution.expression.impl.ExpressionCalculator;
import engine.execution.instance.enitty.EntityInstance;
import engine.execution.instance.property.PropertyInstance;

public class IncreaseAction extends AbstractAction {

    private final String property;
    private final String byExpression;

    public IncreaseAction(EntityDefinition mainEntityDefinition, SecondaryEntityDefinition secondaryEntityDefinition, String property, String byExpression) {
        super(ActionType.DECREASE, mainEntityDefinition, secondaryEntityDefinition);
        this.property = property;
        this.byExpression = byExpression;
    }

    @Override
    public void invoke(Context context) {
        PropertyInstance propertyInstance = context.getPrimaryEntityInstance().getPropertyByName(property);
        if (!verifyNumericPropertyTYpe(propertyInstance)) {
            throw new IllegalArgumentException("increase action can't operate on a none number property " + property);
        }
        PropertyType propertyType = propertyInstance.getPropertyDefinition().getType();
        ExpressionCalculator expressionCalculator = new ExpressionCalculator(byExpression, context, propertyType);
        switch (propertyType) {
            case FLOAT:
                Float floatVal = Float.parseFloat(propertyInstance.getValue().toString());
                Float by = Float.parseFloat(expressionCalculator.calculate().toString());
                Float result = floatVal + by;
                if (propertyInstance.getPropertyDefinition().getValueGenerator().hasRange()) {
                    float to = Float.parseFloat(propertyInstance.getPropertyDefinition().getValueGenerator().getTo().toString());
                    if (to > result) {
                        propertyInstance.updateValue(result, context.getCurrentTick());
                    }
                } else {
                    propertyInstance.updateValue(result, context.getCurrentTick());
                }
                break;
            case DECIMAL:
                Integer intVal = PropertyType.DECIMAL.convert(propertyInstance.getValue());
                Integer byInt = (Integer) expressionCalculator.calculate();
                Integer resultInt = intVal + byInt;
                if (propertyInstance.getPropertyDefinition().getValueGenerator().hasRange()) {
                    int to = Integer.parseInt(propertyInstance.getPropertyDefinition().getValueGenerator().getTo().toString());
                    if (to > resultInt) {
                        propertyInstance.updateValue(resultInt, context.getCurrentTick());
                    }
                } else {
                    propertyInstance.updateValue(resultInt, context.getCurrentTick());
                }
                break;
            default:
                throw new IllegalArgumentException("increase action can't operate on a none number property " + property);
        }
        invokeOnSecondary(context);
    }

    private boolean verifyNumericPropertyTYpe(PropertyInstance propertyValue) {
        return
                PropertyType.DECIMAL.equals(propertyValue.getPropertyDefinition().getType()) || PropertyType.FLOAT.equals(propertyValue.getPropertyDefinition().getType());
    }
}
