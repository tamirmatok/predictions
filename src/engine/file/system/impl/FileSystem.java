package engine.file.system.impl;

import engine.file.system.xml.impl.XmlLoader;
import engine.schema.generated.PRDWorld;
import engine.schema.jaxb.SchemaBasedJAXB;

public class FileSystem implements engine.file.system.api.fileSystemInterface {

    XmlLoader xmlLoader;

    public FileSystem() {
        this.xmlLoader = new XmlLoader();
    }

    @Override
    public void loadXmlFile(String xmlFilePath) {
        this.xmlLoader.load(xmlFilePath);
    }

    @Override
    public PRDWorld loadJaxbWorldFromXmlFile() {
        return new SchemaBasedJAXB(this.xmlLoader.getFile()).getSchemaBasedJAXB();
    }
}
