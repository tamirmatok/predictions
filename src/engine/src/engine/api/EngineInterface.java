package engine.api;

import dto.impl.MessageDTO;
import dto.impl.PRDEnvDTO;
import dto.impl.PrdWorldDTO;
import dto.impl.PropertiesDTO;
import dto.impl.simulation.SimulationDTO;

import java.util.HashMap;

public interface EngineInterface {

    SimulationDTO runSimulation(HashMap<String, String> envPropertyNameToEnvPropertyValue, HashMap<String, String> entityNameToEntityPopulation);
    MessageDTO loadSystemWorldFromXmlFile(String xmlFilePath);
    PrdWorldDTO getLoadedWorldDetails();
    PRDEnvDTO getEnvState();



}
