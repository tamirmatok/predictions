package engine.action.impl.condition.impl;

import engine.action.impl.condition.api.ConditionType;

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

    public ArrayList<Condition> getInnerConditions() {
        return innerConditions;
    }
}
