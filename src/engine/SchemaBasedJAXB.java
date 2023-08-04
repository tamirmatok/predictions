package engine;
import javax.swing.text.html.parser.Entity;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import schema.generated.*;


public class SchemaBasedJAXB {

    private static final String JAXB_XML_PACKAGE_NAME = "schema.generated";

    private final File file;

    public SchemaBasedJAXB(File file) {
        this.file = file;
    }

    public PRDWorld getSchemaBasedJAXB() {
        try {
            InputStream inputStream = new FileInputStream(file);
            return deserializeFrom(inputStream);

        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static PRDWorld deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(in);
    }
}
