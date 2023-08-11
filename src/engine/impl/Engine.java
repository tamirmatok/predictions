package engine.impl;

import dto.impl.MessageDTO;
import engine.api.EngineInterface;
import engine.definition.world.impl.WorldDefinitionImpl;
import engine.execution.instance.world.impl.WorldInstance;
import engine.file.system.impl.FileSystem;
import engine.schema.generated.PRDWorld;

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
            return new MessageDTO(false, e.getMessage());
        }
        return new MessageDTO(true, "Success: Xml file loaded successfully");
    }

    @Override
    public MessageDTO getSimulationState() {
        return null;
    }

}
