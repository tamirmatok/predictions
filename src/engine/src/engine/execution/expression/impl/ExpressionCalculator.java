package engine.execution.expression.impl;

import engine.action.api.Action;
import engine.definition.property.api.PropertyType;
import engine.execution.context.Context;
import engine.execution.expression.api.ExpressionCalculatorInterface;
import engine.execution.instance.enitty.EntityInstance;
import engine.execution.instance.property.PropertyInstance;
import engine.system.functions.SystemFunctions;

import java.util.ArrayList;

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
        PropertyInstance propertyInstance = context.getPrimaryEntityInstance().getPropertyByName(expression);
            if (propertyInstance != null) {
                return propertyInstance.getValue();
            }
        // free value
        return this.validateFreeValueExpression();

    }

    private Object validateFreeValueExpression() {
        if (propertyType == null){
           return expression;
        }
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
        int endInd = expression.lastIndexOf(')');
        String innerExpression = expression.substring(startInd + 1, endInd);
        EntityInstance entityInstance;
        switch (systemFunctionName) {
            case "environment":
                return systemFunctions.environment(context.getEnvironmentVariable(innerExpression));
            case "random":
                Integer to = Integer.parseInt(innerExpression);
                return systemFunctions.random(to);
            case "evaluate":
                String entityName = innerExpression.split("\\.")[0];
                String propertyName = innerExpression.split("\\.")[1];
                entityInstance = this.evaluateEntityInstance(entityName);
                return entityInstance.getPropertyByName(propertyName).getValue();
            case "percent":
                String[] percentArgs = innerExpression.split(",");
                ExpressionCalculator arg1Calculator = new ExpressionCalculator(percentArgs[0], context, PropertyType.FLOAT);
                ExpressionCalculator arg2Calculator = new ExpressionCalculator(percentArgs[1], context, PropertyType.FLOAT);
                Double arg1 = (Double) arg1Calculator.calculate();
                Double arg2 = (Double) arg2Calculator.calculate();
                if (arg2 != 0){
                    return (arg1 / arg2);
                }
                else{
                    return 0;
                }
            case "ticks":
                String entityName1 = innerExpression.split("\\.")[0];
                String propertyName1 = innerExpression.split("\\.")[1];
                entityInstance = context.getEntityInstanceManager().getInstanceByName(entityName1);
                PropertyInstance propertyInstance1 = entityInstance.getPropertyByName(propertyName1);
                return propertyInstance1.getTicksCount(context.getCurrentTick());

            default:
                throw new IllegalArgumentException("System function " + systemFunctionName + " is not supported at this point");
        }
    }

    private EntityInstance evaluateEntityInstance(String entityName) {
        for (Context prevContext : context.getRootContexts()) {
            EntityInstance primaryEntityInstance = prevContext.getPrimaryEntityInstance();
            if (primaryEntityInstance.getEntityDefinition().getName().equals(entityName)) {
                return primaryEntityInstance;
            }
            else{
                for (EntityInstance secondaryEntityInstance : prevContext.getSecondaryEntityInstances()) {
                    if (secondaryEntityInstance.getEntityDefinition().getName().equals(entityName)) {
                        return secondaryEntityInstance;
                    }
                }
            }
        }
        throw new IllegalArgumentException("Entity " + entityName + " is not defined in this context");
    }

}
