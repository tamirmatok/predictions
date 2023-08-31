package engine.action.impl.condition.impl;

import engine.action.impl.condition.api.ConditionType;
import engine.definition.property.api.PropertyType;
import engine.execution.context.Context;
import engine.execution.expression.impl.ExpressionCalculator;

public class SingleCondition extends Condition{

    String entityName;
    String propertyName;
    String operator;

    String valueExpression;
    private final String[] validOperators = {"=", "!=", "bt", "lt"};

    public SingleCondition(String entityName, String propertyName, String operator, String valueExpression) {
        super(ConditionType.SINGLE);
        this.entityName = entityName;
        this.propertyName = propertyName;
        this.operator = operator;
        this.valueExpression = valueExpression;
        this.isValid();
    }

    private void isValid() {
        if (entityName == null || entityName.isEmpty()) {
            throw new IllegalArgumentException("Single condition error - Entity name cannot be null or empty");
        }
        if (propertyName == null || propertyName.isEmpty()) {
            throw new IllegalArgumentException("Single condition error - Property name cannot be null or empty");
        }
        if (operator == null || operator.isEmpty()) {
            throw new IllegalArgumentException("Single condition error - Operator cannot be null or empty");
        }
        if (valueExpression == null || valueExpression.isEmpty()) {
            throw new IllegalArgumentException("Single condition error - Value expression cannot be null or empty");
        }
        boolean validOperator = false;
        for (String op : validOperators) {
            if (op.equals(operator)) {
                validOperator = true;
                break;
            }
        }
        if (!validOperator) {
            throw new IllegalArgumentException("Single condition error - Invalid operator: " + operator);
        }
    }


    public boolean calcCondition(Context context) {
        PropertyType propertyType = context.getPrimaryEntityInstance().getPropertyByName(propertyName).getPropertyDefinition().getType();
        ExpressionCalculator expressionCalculator = new ExpressionCalculator(valueExpression, context, propertyType);

        Object properyValue = context.getPrimaryEntityInstance().getPropertyByName(propertyName).getValue();
        Object expressionValue = expressionCalculator.calculate();

        switch (operator){
            case "=": {
                return properyValue.equals(expressionValue);
            }
            case "!=": {
                return !properyValue.equals(expressionValue);
            }
            case "bt":{
                switch (propertyType){
                    case DECIMAL:
                        return Integer.parseInt(properyValue.toString()) > Integer.parseInt(expressionValue.toString());
                    case FLOAT:
                        return Float.parseFloat(properyValue.toString()) > Float.parseFloat(expressionValue.toString());
                }
            }
            case "lt":{
                switch (propertyType){
                    case DECIMAL:
                        return Integer.parseInt(properyValue.toString()) < Integer.parseInt(expressionValue.toString());
                    case FLOAT:
                        return Float.parseFloat(properyValue.toString()) < Float.parseFloat(expressionValue.toString());
                }
            }
            default:{
                throw new IllegalArgumentException("Invalid operator: " + operator);
            }
        }
    }


    public String getEntityName() {
        return entityName;
    }

    public String getPropName() {
        return propertyName;
    }

    public String getOperator() {
        return operator;
    }

    public String getValueExpression() {
        return valueExpression;
    }

    @Override
    public String toString() {
        return "SingleCondition [entityName=" + entityName + ", propName=" + propertyName + ", operator=" + operator
                + ", valueExpression=" + valueExpression + "]";
    }



}
