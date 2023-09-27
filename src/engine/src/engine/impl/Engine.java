package engine.impl;

import dto.impl.MessageDTO;
import dto.impl.PRDEnvDTO;
import dto.impl.PrdWorldDTO;
import dto.impl.simulation.SimulationDTO;
import dto.impl.simulation.SimulationExecutionDetails;
import engine.api.EngineInterface;
import engine.definition.world.api.WorldDefinition;
import engine.definition.world.impl.WorldDefinitionImpl;
import engine.schema.generated.PRDEnvironment;
import engine.simulation.manager.SimulationExecutionManager;
import engine.system.file.impl.FileSystem;
import engine.schema.generated.PRDWorld;
import java.util.HashMap;

public class Engine implements EngineInterface {

    private PRDWorld prdWorld;
    private WorldDefinition worldDefinition;
    private boolean isWorldLoaded;
    private SimulationExecutionManager simulationExecutionManager;


    public Engine() {
        this.isWorldLoaded = false;
    }

    // Load world from xml file
    @Override
    public MessageDTO loadSystemWorldFromXmlFile(String xmlFilePath) {
        try {
            FileSystem fileSystem = new FileSystem(xmlFilePath);
            fileSystem.loadXmlFile();
            this.prdWorld = fileSystem.loadJaxbWorldFromXmlFile();
            this.worldDefinition = new WorldDefinitionImpl(prdWorld);
            this.simulationExecutionManager = new SimulationExecutionManager(prdWorld.getPRDThreadCount());
            isWorldLoaded = true;

            //TODO: At this phase all file validation should be done - Check it !!

        } catch (Exception e) {
            this.prdWorld = null;
            this.worldDefinition = null;
            this.isWorldLoaded = false;
            return new MessageDTO(false, "Failed to load system xml file - " + e.getMessage());
        }
        return new MessageDTO(true, "Xml file loaded successfully");
    }

    // Get world details
    @Override
    public PrdWorldDTO getLoadedWorldDetails() {
        if (!isWorldLoaded) {
            return new PrdWorldDTO(false, new PRDWorld());
        }
        return new PrdWorldDTO(true, prdWorld);
    }

    @Override
    public SimulationDTO runSimulation(HashMap<String, String> envPropertyNameToEnvPropertyValue, HashMap<String, String> entityNameToEntityPopulation) {
        if (isWorldLoaded){
                return simulationExecutionManager.startNewSimulation(this.worldDefinition, envPropertyNameToEnvPropertyValue, entityNameToEntityPopulation);
            } else {
                return new SimulationDTO(false, null);
            }
    }

    @Override
    public PRDEnvDTO getEnvState() {
        if (!isWorldLoaded) {
            return new PRDEnvDTO(false, new PRDEnvironment());
        } else {
            return new PRDEnvDTO(true, prdWorld.getPRDEnvironment());
        }
    }

    public HashMap<Integer, SimulationExecutionDetails> getSimulationsExecutionDetails() {
        if (simulationExecutionManager == null) {
            return null;
        }
        return simulationExecutionManager.getSimulationIdToSimulationExecutionDetails();
    }

    public void pauseSimulation(Integer simulationId) {
        simulationExecutionManager.pauseSimulation(simulationId);
    }

    public void resumeSimulation(Integer simulationId) {
        simulationExecutionManager.resumeSimulation(simulationId);
    }

    public void stopSimulation(Integer simulationId) {
        simulationExecutionManager.terminateSimulation(simulationId);
    }


}
