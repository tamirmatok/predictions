package engine.system.expression.impl;

import engine.execution.context.Context;
import engine.system.expression.api.eExpression;

public class EnvironmentFunction extends FunctionExpression {
    public EnvironmentFunction(String argExp) {
        super(argExp, eExpression.FUNCTION);
    }

    @Override
    public Object calculateExpression(Context context) {
        return context.getEnvironmentVariable(argExp).getValue();
    }
}
