package engine.execution.expression.impl;

import engine.definition.property.api.PropertyType;
import engine.execution.context.Context;
import engine.execution.expression.api.ExpressionCalculatorInterface;
import engine.execution.instance.enitty.EntityInstance;
import engine.execution.instance.property.PropertyInstance;
import engine.system.functions.SystemFunctions;

public class ExpressionCalculator implements ExpressionCalculatorInterface {
    String expression;
    SystemFunctions systemFunctions;
    Context context;
    PropertyType propertyType;

    public ExpressionCalculator(String expression, Context context, PropertyType propertyType){
        this.expression = expression;
        this.context = context;
        this.propertyType = propertyType;
        this.systemFunctions = new SystemFunctions();
    }

    @Override
    public Object calculate() {
        validateExpression(expression);
        // case 1: if it is a system function expression
        for (String systemFunctionName : systemFunctions.getSystemFunctionNames()) {
            if (expression.startsWith(systemFunctionName)) {
                return this.calcSystemFunctionExpression(systemFunctionName, expression);
            }
        }
        // case2 2: if it is main entity property expression
        EntityInstance primaryEntityInstance = context.getPrimaryEntityInstance();
        PropertyInstance propertyInstance = primaryEntityInstance.getPropertyByName(expression);
        if (propertyInstance != null) {
            return propertyInstance.getValue();
        }
        // free value
        return this.validateFreeValueExpression();


    }

    private Object validateFreeValueExpression() {
        try {
            switch (propertyType) {
                case DECIMAL:
                    return Integer.parseInt(expression);
                case BOOLEAN:
                    return Boolean.parseBoolean(expression);
                case FLOAT:
                    return Float.parseFloat(expression);
                default:
                    return expression;
            }
        }
        catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private void validateExpression(String expression) {
        if (expression == null || expression.isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be empty");
        }
    }


    Object calcSystemFunctionExpression(String systemFunctionName, String expression) {
        boolean validExpression = expression.startsWith(systemFunctionName + '(') && expression.endsWith(")");
        if (!validExpression) {
            throw new IllegalArgumentException("System function " + systemFunctionName + " has invalid expression " + expression);
        }
        int startInd = expression.indexOf('(');
        int endInd = expression.indexOf(')');
        String innerExpression = expression.substring(startInd + 1, endInd);

        switch (systemFunctionName) {
            case "environment":
                return systemFunctions.environment(context.getEnvironmentVariable(innerExpression));
            case "random":
                Integer to = Integer.parseInt(innerExpression);
                return systemFunctions.random(to);
                //TODO : add the rest of the functions.
            default:
                throw new IllegalArgumentException("System function " + systemFunctionName + " is not supported at this point");
        }
    }

}
