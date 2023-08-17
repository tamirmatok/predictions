package engine.action.impl;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.definition.entity.EntityDefinition;
import engine.definition.property.api.PropertyType;
import engine.execution.context.Context;
import engine.execution.expression.impl.ExpressionCalculator;
import engine.execution.instance.property.PropertyInstance;

public class DecreaseAction extends AbstractAction {

    private final String property;
    private final String byExpression;

    public DecreaseAction(EntityDefinition entityDefinition, String property, String byExpression) {
        super(ActionType.DECREASE, entityDefinition);
        this.property = property;
        this.byExpression = byExpression;
    }

    @Override
    public void invoke(Context context) {
        PropertyInstance propertyInstance = context.getPrimaryEntityInstance().getPropertyByName(property);
        if (!verifyNumericPropertyTYpe(propertyInstance)) {
            throw new IllegalArgumentException("decrease action can't operate on a none number property " + property);
        }
        PropertyType propertyType = propertyInstance.getPropertyDefinition().getType();
        ExpressionCalculator expressionCalculator = new ExpressionCalculator(byExpression, context, propertyType);
        switch (propertyType){
            case FLOAT:
                Float floatVal = PropertyType.FLOAT.convert(propertyInstance.getValue());
                Float by = (Float) expressionCalculator.calculate(byExpression);
                Float result = floatVal - by;
                propertyInstance.updateValue(result);
                break;
            case DECIMAL:
                Double intVal = PropertyType.DECIMAL.convert(propertyInstance.getValue());
                Double byDouble = (Double) expressionCalculator.calculate(byExpression);
                Double resultDouble = intVal - byDouble;
                propertyInstance.updateValue(resultDouble);
                break;
            default:
                throw new IllegalArgumentException("decrease action can't operate on a none number property " + property);
        }
    }

    private boolean verifyNumericPropertyTYpe(PropertyInstance propertyValue) {
        return
                PropertyType.DECIMAL.equals(propertyValue.getPropertyDefinition().getType()) || PropertyType.FLOAT.equals(propertyValue.getPropertyDefinition().getType());
    }
}
