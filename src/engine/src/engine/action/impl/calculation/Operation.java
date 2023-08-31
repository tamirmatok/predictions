package engine.action.impl.calculation;

public class Operation {

    String arg1Experssion;
    String arg2Experssion;
    String operation;

    public String getArg1Experssion() {
        return arg1Experssion;
    }

    public String getArg2Experssion() {
        return arg2Experssion;
    }

    public String getOperation() {
        return operation;
    }

    public void setArg1Experssion(String arg1Experssion) {
        this.arg1Experssion = arg1Experssion;
    }

    public void setArg2Experssion(String arg2Experssion) {
        this.arg2Experssion = arg2Experssion;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String toString() {
        return "Operation: " + arg1Experssion + " " + operation + " " + arg2Experssion;
    }
}
