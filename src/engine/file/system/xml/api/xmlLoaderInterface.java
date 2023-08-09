package engine.file.system.xml.api;

import java.io.File;

public interface xmlLoaderInterface {
    void setPath(String filePath);
    File getFile();
    boolean isValidXMLPath();
}
