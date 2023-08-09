package engine.file.system.xml.impl;
import java.io.File;


public class XmlLoader {
    private String filePath;
    private File file;

    public XmlLoader() {
        this.filePath = "";
        this.file = null;
    }

    public XmlLoader(String filePath) {
        this.load(filePath);
    }

    public void load(String filePath) {
        this.filePath = filePath;
        this.file = new File(this.filePath);
        boolean validPath = this.isValidXMLPath();
        if (!validPath) {
            throw new IllegalArgumentException("Invalid XML path");
        }
    }

    public File getFile(){
        return this.file;
    }
    public boolean isValidXMLPath() {
        return file.exists() && file.isFile();
    }


}
