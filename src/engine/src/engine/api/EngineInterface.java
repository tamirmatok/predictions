package engine.api;

import dto.impl.MessageDTO;
import dto.impl.PRDEnvDTO;
import dto.impl.PrdWorldDTO;
import dto.impl.PropertiesDTO;

import java.util.HashMap;

public interface EngineInterface {

    MessageDTO runSimulation(HashMap<String, Object> envPropertyNameToEnvPropertyValue);
    MessageDTO loadSystemWorldFromXmlFile(String xmlFilePath);
    PrdWorldDTO getLoadedWorldDetails();
    PRDEnvDTO getEnvState();



}
