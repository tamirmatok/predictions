package engine.impl;

import dto.impl.MessageDTO;
import dto.impl.PRDEnvDTO;
import dto.impl.PrdWorldDTO;
import engine.api.EngineInterface;
import engine.definition.world.impl.WorldDefinitionImpl;
import engine.execution.instance.world.impl.WorldInstance;
import engine.schema.generated.PRDEvironment;
import engine.system.file.impl.FileSystem;
import engine.schema.generated.PRDWorld;

import java.util.Arrays;

public class Engine implements EngineInterface {

    private final FileSystem fileSystem;
    private  WorldDefinitionImpl worldDefinition;
    private WorldInstance worldInstance;
    private boolean isWorldLoaded = false;
    private PRDWorld prdWorld;


    public Engine() {
        this.fileSystem = new FileSystem();
        this.worldDefinition = new WorldDefinitionImpl();

    };

    // option number 1 - load world from xml file
    @Override
    public MessageDTO loadSystemWorldFromXmlFile(String xmlFilePath){
        try {
            this.fileSystem.loadXmlFile(xmlFilePath);
            this.prdWorld = this.fileSystem.loadJaxbWorldFromXmlFile();
            this.worldDefinition.loadWorldDefintion(prdWorld);
            isWorldLoaded = true;

        } catch (Exception e) {
            this.resetWorld();
            return new MessageDTO(false, e.toString());
        }
        this.worldInstance = new WorldInstance(this.worldDefinition);
        return new MessageDTO(true, "Xml file loaded successfully");
    }

    // option number 2 - get simulation state
    @Override
    public PrdWorldDTO getSimulationState() {
        if (!isWorldLoaded) {
            return new PrdWorldDTO(false, new PRDWorld());
        }
        return new PrdWorldDTO(true, prdWorld);
    }

    @Override
    public PRDEnvDTO getEnvState() {
        if (!isWorldLoaded) {
            return new PRDEnvDTO(false, new PRDEvironment());
        }
        else{
            return new PRDEnvDTO(true, prdWorld.getPRDEvironment());
        }
    }

    @Override
    public MessageDTO setEnvVariable(String envVariableName, Object value) {
        return this.worldInstance.setEnvVariable(envVariableName, value);
    }

    private void resetWorld(){
        this.prdWorld = new PRDWorld();
        this.worldDefinition = new WorldDefinitionImpl();
    }

    public MessageDTO runSimulation() {
        try {
            return this.worldInstance.runSimulation();
        }
        catch(Exception e){
            return new MessageDTO(false, e.getMessage());
        }
    }
}
