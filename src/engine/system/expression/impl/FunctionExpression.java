package engine.system.expression.impl;

import engine.execution.context.Context;
import engine.system.expression.api.AbstractExpression;
import engine.system.expression.api.eExpression;

public abstract class FunctionExpression extends AbstractExpression {

    protected String argExp;
    public FunctionExpression(String argExp, eExpression typeExpression) {
        super(typeExpression);
        this.argExp = argExp;
    }

    public abstract Object calculateExpression(Context context);
}
