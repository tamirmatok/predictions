package engine.system.expression.api;

import engine.execution.context.Context;

public interface Expression {
    Object calculateExpression(Context context);
}
