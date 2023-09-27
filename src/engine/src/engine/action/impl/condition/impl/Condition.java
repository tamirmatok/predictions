package engine.action.impl.condition.impl;

import engine.action.impl.condition.api.ConditionType;
import engine.execution.context.Context;

public abstract class Condition {
    ConditionType conditionType;
    public Condition(ConditionType conditionType) {
        this.conditionType = conditionType;
    }
    public ConditionType getConditionType() {
        return conditionType;
    }

    public boolean calcCondition(Context context) {
        return false;
    }
}
