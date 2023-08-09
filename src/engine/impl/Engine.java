package engine.impl;

import engine.api.EngineInterface;
import engine.definition.world.api.WorldDefinition;
import engine.definition.world.impl.WorldDefinitionImpl;
import engine.execution.instance.world.impl.WorldInstance;
import engine.file.system.impl.FileSystem;
import engine.schema.generated.PRDWorld;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class Engine implements EngineInterface {

    FileSystem fileSystem;
    WorldInstance worldInstance;

    public Engine() {
        this.fileSystem = new FileSystem();
        this.worldInstance = new WorldInstance(new WorldDefinitionImpl());
    };
    @Override
    public void loadSystemWorldFromXmlFile(String xmlFilePath) throws JAXBException, IOException {
        this.fileSystem.loadXmlFile(xmlFilePath);
        PRDWorld prdWorld = this.fileSystem.loadJaxbWorldFromXmlFile();
        worldInstance.setWorld(prdWorld);
    }
}
