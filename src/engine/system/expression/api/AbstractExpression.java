package engine.system.expression.api;

public abstract class AbstractExpression implements Expression{
    protected eExpression typeExpression;

    public AbstractExpression(eExpression eExp){
        this.typeExpression = eExp;
    }

}
