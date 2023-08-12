package engine.system.file.api;

import engine.schema.generated.PRDWorld;

public interface fileSystemInterface {
    void loadXmlFile(String xmlFilePath);
    PRDWorld loadJaxbWorldFromXmlFile();
}
