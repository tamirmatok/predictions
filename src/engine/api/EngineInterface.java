package engine.api;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface EngineInterface {
    void loadSystemWorldFromXmlFile(String xmlFilePath) throws JAXBException, IOException;
}
