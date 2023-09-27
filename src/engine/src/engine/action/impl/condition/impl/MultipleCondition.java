package engine.action.impl.condition.impl;

import engine.action.impl.condition.api.ConditionType;
import engine.execution.context.Context;

import java.util.ArrayList;

public class MultipleCondition extends Condition{

    private final String logical;
    private final String[] logicalOperatoes = {"and", "or"};

    private final ArrayList<Condition> innerConditions;

    public MultipleCondition(String logical, ArrayList<Condition> innerConditions) {
        super(ConditionType.MULTIPLE);
        this.innerConditions = innerConditions;
        this.logical = logical;
        this.isValid();
    }

    public String getLogical() {
        return logical;
    }

    private void isValid(){
        if (logical == null || logical.isEmpty()) {
            throw new IllegalArgumentException("Multiple condition error - Logical operator cannot be null or empty");
        }
        boolean validOperator = false;
        for (String op : logicalOperatoes) {
            if (op.equals(logical)) {
                validOperator = true;
                break;
            }
        }
        if (!validOperator) {
            throw new IllegalArgumentException("Multiple condition error - Invalid logical operator");
        }
    }


    @Override
    public boolean calcCondition(Context context) {
        boolean result = this.initResult();

        for (Condition condition : innerConditions) {
            switch (condition.getConditionType()){
                case SINGLE: {
                    SingleCondition singleCondition = (SingleCondition) condition;
                    boolean conditionResult = singleCondition.calcCondition(context);
                    result = updateResult(result, conditionResult);
                    break;
                }
                case MULTIPLE:
                    MultipleCondition multipleCondition = (MultipleCondition) condition;
                    boolean conditionResult = multipleCondition.calcCondition(context);
                    result = updateResult(result, conditionResult);
                    break;
            }
            if (!result && logical.equals("and")){
                return false;
            }
        }
        return result;
    }

    private boolean initResult(){
        switch (logical) {
            case "and": {
                return true;
            }
            case "or": {
                return false;
            }
            default: {
                throw new IllegalArgumentException("Multiple condition error - Invalid logical operator");
            }
        }
    }

    private boolean updateResult(boolean result, boolean conditionResult){
        switch (logical) {
            case "and": {
                return (result && conditionResult);
            }
            case "or": {
                return (result || conditionResult);
            }
            default: {
                throw new IllegalArgumentException("Multiple condition error - Invalid logical operator");
            }
        }
    }


    public ArrayList<Condition> getInnerConditions() {
        return innerConditions;
    }
}
