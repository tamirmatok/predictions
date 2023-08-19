package engine.api;

import dto.impl.MessageDTO;
import dto.impl.PRDEnvDTO;
import dto.impl.PrdWorldDTO;
import dto.impl.PropertiesDTO;

public interface EngineInterface {
    MessageDTO loadSystemWorldFromXmlFile(String xmlFilePath);
    PrdWorldDTO getSimulationState();

    PRDEnvDTO getEnvState();

    MessageDTO setEnvVariable(String envVariableName, Object value);

    PropertiesDTO setEnvVariables();

    MessageDTO resetActiveEnvironment();

    MessageDTO startSimulation();
}
