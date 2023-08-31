package engine.impl;

import dto.impl.MessageDTO;
import dto.impl.PRDEnvDTO;
import dto.impl.PrdWorldDTO;
import engine.api.EngineInterface;
import engine.definition.property.api.PropertyDefinition;
import engine.definition.world.api.WorldDefinition;
import engine.definition.world.impl.WorldDefinitionImpl;
import engine.execution.instance.property.PropertyInstance;
import engine.execution.instance.property.PropertyInstanceImpl;
import engine.schema.generated.PRDEnvironment;
import engine.simulation.simulator.impl.Simulator;
import engine.system.file.impl.FileSystem;
import engine.schema.generated.PRDWorld;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Engine implements EngineInterface {

    private PRDWorld prdWorld;
    private WorldDefinition worldDefinition;
    HashMap<Integer, Simulator> simulationIdToSimulator;
    private boolean isWorldLoaded;
    private ExecutorService threadPool;


    public Engine() {
        this.simulationIdToSimulator = new HashMap<Integer, Simulator>();
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
            this.threadPool = Executors.newFixedThreadPool(prdWorld.getPRDThreadCount());
            isWorldLoaded = true;
            //TODO: At this phase all file validation should be done - Check it !!

        } catch (Exception e) {
            this.resetWorld();
            return new MessageDTO(false, e.getMessage());
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
    public MessageDTO runSimulation(HashMap<String, Object> envPropertyNameToEnvPropertyValue) {
        try {
            if (isWorldLoaded){
                Simulator simulator = this.setEnvProperties(envPropertyNameToEnvPropertyValue);
                this.threadPool.execute(simulator);
                return new MessageDTO(true, simulator.getSimulationId().toString());
            } else {
                return new MessageDTO(false, "World did not load yet.");
            }
        } catch (Exception e) {
            return new MessageDTO(false, e.getMessage());
        }
    }

    private Simulator setEnvProperties(HashMap<String, Object> envPropertyNameToEnvPropertyValue) {
        Simulator simulator = new Simulator(this.worldDefinition);
        Integer simulationId = simulator.getSimulationId();
        for (String envPropertyName : envPropertyNameToEnvPropertyValue.keySet()) {
            Object envPropertyValue = envPropertyNameToEnvPropertyValue.get(envPropertyName);
            PropertyDefinition propertyDefinition = simulator.getWorldInstance().getActiveEnvironment().getProperty(envPropertyName).getPropertyDefinition();
            PropertyInstance propertyInstance = new PropertyInstanceImpl(propertyDefinition, envPropertyValue);
            simulator.getWorldInstance().getActiveEnvironment().addPropertyInstance(propertyInstance);
        }

        simulationIdToSimulator.put(simulationId, simulator);
        return simulator;
    }


    @Override
    public PRDEnvDTO getEnvState() {
        if (!isWorldLoaded) {
            return new PRDEnvDTO(false, new PRDEnvironment());
        } else {
            return new PRDEnvDTO(true, prdWorld.getPRDEnvironment());
        }
    }


    private void resetWorld(){
        this.prdWorld = null;
        this.worldDefinition = null;
        this.isWorldLoaded = false;
    }

//    public SimulationDTO getSimulationReports(){
//        return new SimulationDTO(true, simulationReports);
//    }


}
