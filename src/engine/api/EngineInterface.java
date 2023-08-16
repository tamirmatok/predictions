package engine.api;

import dto.impl.MessageDTO;
import dto.impl.PRDEnvDTO;
import dto.impl.PrdWorldDTO;

public interface EngineInterface {
    MessageDTO loadSystemWorldFromXmlFile(String xmlFilePath);
    PrdWorldDTO getSimulationState();

    PRDEnvDTO getEnvState();

    MessageDTO setEnvVariable(String envVariableName, Object value);
}
