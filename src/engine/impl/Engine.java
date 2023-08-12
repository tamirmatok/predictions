package engine.impl;

import dto.impl.MessageDTO;
import dto.impl.PrdWorldDTO;
import engine.api.EngineInterface;
import engine.definition.world.impl.WorldDefinitionImpl;
import engine.execution.instance.world.impl.WorldInstance;
import engine.system.file.impl.FileSystem;
import engine.schema.generated.PRDWorld;

import java.util.Arrays;

public class Engine implements EngineInterface {

    private final FileSystem fileSystem;
    private final WorldDefinitionImpl worldDefinition;
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
            MessageDTO message = new MessageDTO(false, e.toString());

            //TODO: REMOVE WHEN DEVELOPING PROCESS FINISHED - FOR DEBUGGING PURPOSES ONLY
            System.out.println(Arrays.toString(e.getStackTrace()));
            return message;
        }
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
}
