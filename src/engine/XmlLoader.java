package engine;

import java.io.File;

public class XmlLoader {
    private String filePath;

    public XmlLoader(String filePath) {

        this.filePath = filePath;
        boolean validPath = this.isValidXMLPath();
        if (!validPath) {
            throw new IllegalArgumentException("Invalid XML path");
        }
    }

    public void loadNewFile(String filePath) {
        this.filePath = filePath;
    }

    public boolean isValidXMLPath() {
        File file = new File(this.filePath);
        return file.exists() && file.isFile();
    }

}
