package engine.action.impl.condition.impl;

import engine.action.impl.condition.api.ConditionType;

public abstract class Condition {
    ConditionType conditionType;

    public Condition(ConditionType conditionType) {
        this.conditionType = conditionType;
    }
    public ConditionType getConditionType() {
        return conditionType;
    }
}
