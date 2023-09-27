package dto.impl.simulation;

import dto.api.AbstractDTO;

import java.util.ArrayList;

public class SimulationDTO extends AbstractDTO<SimulationExecutionDetails> {

    private String errorMessage;
    public SimulationDTO(boolean success, SimulationExecutionDetails data) {
        super(success, data);
    }
    public SimulationExecutionDetails getSimulationExecutionDetails() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
