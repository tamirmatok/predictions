package engine.api;

import dto.impl.MessageDTO;

public interface EngineInterface {
    MessageDTO loadSystemWorldFromXmlFile(String xmlFilePath);
    MessageDTO getSimulationState();
}
