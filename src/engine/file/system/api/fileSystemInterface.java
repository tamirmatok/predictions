package engine.file.system.api;

import engine.file.system.xml.impl.XmlLoader;
import engine.schema.generated.PRDWorld;

public interface fileSystemInterface {
    void loadXmlFile(String xmlFilePath);
    PRDWorld loadJaxbWorldFromXmlFile();
}
