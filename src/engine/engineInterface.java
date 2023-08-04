package engine;

import schema.generated.PRDWorld;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

public interface engineInterface {

    void loadSystemFromXmlFile(File xmlFile) throws JAXBException, IOException;
    PRDWorld getJaxbWorld();

}
