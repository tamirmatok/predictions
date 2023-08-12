package engine.action.impl.condition.impl;

import engine.action.impl.condition.api.ConditionType;

public abstract class Condition {
    ConditionType conditionType;
    private final String[] logicalOperatoes = {"and", "or"};

    public ConditionType getConditionType() {
        return conditionType;
    }

    public Condition(ConditionType conditionType) {
        this.conditionType = conditionType;
    }
}
