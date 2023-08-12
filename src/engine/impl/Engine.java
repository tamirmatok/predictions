package engine.impl;

import dto.impl.MessageDTO;
import engine.api.EngineInterface;
import engine.definition.world.impl.WorldDefinitionImpl;
import engine.execution.instance.world.impl.WorldInstance;
import engine.system.file.impl.FileSystem;
import engine.schema.generated.PRDWorld;

import java.util.Arrays;

public class Engine implements EngineInterface {

    FileSystem fileSystem;
    WorldDefinitionImpl worldDefinition;
    WorldInstance worldInstance;

    public Engine() {
        this.fileSystem = new FileSystem();
        this.worldDefinition = new WorldDefinitionImpl();

    };
    @Override
    public MessageDTO loadSystemWorldFromXmlFile(String xmlFilePath){
        try {
            this.fileSystem.loadXmlFile(xmlFilePath);
            PRDWorld prdWorld = this.fileSystem.loadJaxbWorldFromXmlFile();
            this.worldDefinition.loadWorldDefintion(prdWorld);

        } catch (Exception e) {
            MessageDTO message = new MessageDTO(false, e.toString());
            //TODO: REMOVE WHEN DEVELOPING PROCESS FINISHED - FOR DEBUGGING PURPOSES ONLY
            System.out.println(Arrays.toString(e.getStackTrace()));
            return message;
        }
        return new MessageDTO(true, "Xml file loaded successfully");
    }

    @Override
    public MessageDTO getSimulationState() {
        return null;
    }

}
