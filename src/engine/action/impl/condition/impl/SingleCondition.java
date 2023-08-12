package engine.action.impl.condition.impl;

import engine.action.impl.condition.api.ConditionType;

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
