package model.databaseManager;

import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public interface XMLWriter {
    /**
     * This method will write provided {@link org.w3c.dom.Document} to pointed {@link java.io.File}
     * @param dbFile {@link java.io.File} where method will write data.
     * @param doc {@link org.w3c.dom.Document} which contains data.
     * @return true when operation is succesfull.
     */
    default boolean updateDBFile(File dbFile, Document doc) {
        try (FileOutputStream outputStream = new FileOutputStream(dbFile)) {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(new DOMSource(doc), new StreamResult(outputStream));
        } catch (TransformerException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
