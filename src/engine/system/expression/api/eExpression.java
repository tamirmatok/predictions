package engine.system.expression.api;

public enum eExpression {
    FUNCTION("function"),
    ENVIRONMENT("environment"),
    RANDOM("random");
    public final String functionInString;

    eExpression(String functionInString) {
        this.functionInString = functionInString;
    }

}
