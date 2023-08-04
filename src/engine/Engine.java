package engine;

import engine.world.World;
import schema.generated.PRDWorld;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

public class Engine implements engineInterface {

    private World world;

    public Engine() {
        this.world = new World();
    }

    public void loadSystemFromXmlFile(File xmlFile) throws JAXBException, IOException {
        PRDWorld jaxbPrdWorld = new SchemaBasedJAXB(xmlFile).getSchemaBasedJAXB();
        this.world.setWorld(jaxbPrdWorld);
    }

    public World getWorld() {
        return this.world;
    }



}
