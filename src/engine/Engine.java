package engine;

import schema.generated.PRDWorld;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

public class Engine implements engineInterface {

    private PRDWorld jaxbPrdWorld;

    public void loadSystemFromXmlFile(File xmlFile) throws JAXBException, IOException {
        this.jaxbPrdWorld = new SchemaBasedJAXB(xmlFile).getSchemaBasedJAXB();
    }

    public PRDWorld getJaxbWorld() {
        return jaxbPrdWorld;
    }
}
