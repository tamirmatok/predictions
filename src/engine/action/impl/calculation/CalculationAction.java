package engine.action.impl.calculation;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.definition.entity.EntityDefinition;
import engine.execution.context.Context;

public class CalculationAction extends AbstractAction {

    private final String resultProp;
    private final Operation operation;


    public CalculationAction(EntityDefinition entityDefinition, String resultProp, Operation operation) {
        super(ActionType.CALCULATION, entityDefinition);
        this.resultProp = resultProp;
        this.operation = operation;
    }

    public String getResultProp() {
        return resultProp;
    }

    public Operation getOperation() {
        return operation;
    }

    @Override
    public void invoke(Context context) {
    }
}
