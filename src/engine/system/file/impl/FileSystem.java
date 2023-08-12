package engine.system.file.impl;

import engine.system.file.api.fileSystemInterface;
import engine.system.file.xml.impl.XmlLoader;
import engine.schema.generated.PRDWorld;
import engine.schema.jaxb.SchemaBasedJAXB;

public class FileSystem implements fileSystemInterface {

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
