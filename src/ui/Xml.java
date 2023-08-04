package ui;
import java.io.File;


public class Xml {
    private String filePath;
    private File file;

    public Xml() {
        this.filePath = "";
        this.file = null;
    }

    public Xml(String filePath) {
        this.filePath = filePath;
        boolean validPath = this.isValidXMLPath();
        if (!validPath) {
            throw new IllegalArgumentException("Invalid XML path");
        }
        this.file = new File(this.filePath);
    }

    public void setPath(String filePath) {
        this.filePath = filePath;
        this.file = new File(this.filePath);
    }

    public File getFile(){
        return this.file;
    }
    public boolean isValidXMLPath() {
        return file.exists() && file.isFile();
    }


}
