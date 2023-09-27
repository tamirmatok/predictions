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

import java.util.ArrayList;

public class DecreaseAction extends AbstractAction {

    private final String property;
    private final String byExpression;

    public DecreaseAction(EntityDefinition mainEntityDefinition, String property, String byExpression) {
        super(ActionType.DECREASE, mainEntityDefinition);
        this.property = property;
        this.byExpression = byExpression;
    }

    public DecreaseAction(EntityDefinition mainEntityDefinition, SecondaryEntityDefinition secondaryEntityDefinition, String property, String byExpression) {
        super(ActionType.DECREASE, mainEntityDefinition, secondaryEntityDefinition);
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
                Float floatVal = Float.parseFloat(propertyInstance.getValue().toString());
                Float by = Float.parseFloat(expressionCalculator.calculate().toString());
                Float result = floatVal - by;
                if (propertyInstance.getPropertyDefinition().getValueGenerator().hasRange()) {
                    float from = Float.parseFloat(propertyInstance.getPropertyDefinition().getValueGenerator().getFrom().toString());
                    if (from < result) {
                        propertyInstance.updateValue(result, context.getCurrentTick());
                    }
                } else {
                    propertyInstance.updateValue(result, context.getCurrentTick());
                }
                break;
            case DECIMAL:
                Integer intVal =Integer.parseInt(propertyInstance.getValue().toString());
                Integer byInt = Integer.parseInt((expressionCalculator.calculate().toString()));
                Integer resultInt = intVal - byInt;
                if (propertyInstance.getPropertyDefinition().getValueGenerator().hasRange()) {
                    int from = Integer.parseInt(propertyInstance.getPropertyDefinition().getValueGenerator().getFrom().toString());
                    if (from < resultInt) {
                        propertyInstance.updateValue(resultInt, context.getCurrentTick());
                    }
                } else {
                    propertyInstance.updateValue(resultInt, context.getCurrentTick());
                }
                break;
            default:
                throw new IllegalArgumentException("decrease action can't operate on a none number property " + property);
        }

        invokeOnSecondary(context);
    }

    private boolean verifyNumericPropertyTYpe(PropertyInstance propertyValue) {
        return
                PropertyType.DECIMAL.equals(propertyValue.getPropertyDefinition().getType()) || PropertyType.FLOAT.equals(propertyValue.getPropertyDefinition().getType());
    }
}
