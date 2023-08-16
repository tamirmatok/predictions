//package engine.system.expression.impl;
//
//import engine.definition.property.api.PropertyDefinition;
//import engine.definition.property.api.PropertyType;
//import engine.system.expression.api.ExpressionCalculatorInterface;
//import engine.system.functions.SystemFunctions;
//import javafx.beans.property.Property;
//
//import java.util.ArrayList;
//
//public class ExpressionCalculator implements ExpressionCalculatorInterface {
//    String expression;
//    SystemFunctions systemFunctions;
//    public ExpressionCalculator(String expression){
//        this.expression = expression;
//        this.systemFunctions = new SystemFunctions();
//    }
//
//    @Override
//    public Object calculate(String expression) {
//        validateExpression(expression);
//        for (String systemFunctionName : systemFunctions.getSystemFunctionNames()) {
//            // Check if it is a system function
//            if (expression.startsWith(systemFunctionName)) {
//                return this.calcSystemFunctionExpression(systemFunctionName, expression);
//            }
////            else if (eck if it is a property in the context of the main entity
////
////            }
//
//        }
//        return null;
//    }
//
//    private void validateExpression(String expression) {
//        if (expression == null || expression.isEmpty()) {
//            throw new IllegalArgumentException("Expression cannot be empty");
//        }
//    }
//
//
//    PropertyType calcSystemFunctionExpression(String systemFunctionName, String expression) {
//        boolean validExpression = expression.startsWith(systemFunctionName + '(') && expression.endsWith(")");
//        if (!validExpression) {
//            throw new IllegalArgumentException("System function " + systemFunctionName + " has invalid expression " + expression);
//        }
//        switch (systemFunctionName) {
//            case "increase":
//                try{
//                    String innerExpression = expression.substring(systemFunctionName.length() + 1, expression.length() - 1);
//                    //TODO: validate if innerExpression is must be a number
//                    int byNumber = Integer.parseInt(innerExpression);
//                    // return increase(entityDefinition, byNumber);
//                    PropertyType propertyType = PropertyType.DECIMAL;
//                    propertyType.convert(byNumber);
//
//                }
//                catch (NumberFormatException e){
//                    throw new IllegalArgumentException("System function " + systemFunctionName + " has invalid expression " + expression);
//                }
//
//                break;
//            case "decrease":
//                break;
//            case "calculation":
//                break;
//            case "condition":
//                break;
//            case "set":
//                break;
//            case "kill":
//                break;
//            case "clear":
//                break;
//            case "replace":
//                break;
//            case "proximity":
//                break;
//            default:
//                throw new IllegalArgumentException("System function " + systemFunctionName + " is not supported");
//        }
//    }
//
//}
