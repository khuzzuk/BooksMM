package html;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public interface XMLParser {
    Logger logger = Logger.getLogger(XMLParser.class);

    /**
     * This method will create new blank {@link org.w3c.dom.Document}.
     * @return {@link org.w3c.dom.Document} without any data.
     */
    default Document getDocument(){
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            logger.error("Failed to provide new instance of Document class object. Examine XMLParser class from source code.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method will parse content from stream into xml data. Successful operation
     * will return {@link org.w3c.dom.Document} object with proper structure. It will try to close
     * provided stream, and when it be unsuccessful it will throw {@link IOException}.
     * @param stream {@link java.io.InputStream} object which should contain xml data structure.
     * @return {@link org.w3c.dom.Document} with data provided in stream.
     */
    default Document getDocument(InputStream stream) throws IOException {
        Document doc = null;
        //XXX: test after refactoring, add system.exit call if you wish to close the app or throw a runtime exception
        try (InputStream is = stream){
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
        } catch (ParserConfigurationException e) {
            logger.error("Failed to provide new instance of Document class object. Examine XMLParser class from source code, exception says " + e.getMessage());
        } catch (SAXException e) {
            logger.error("Found malformed content during xml parsing operation, exception says " + e.getMessage());
        } catch (IOException e) {
            logger.error("Failed to connect with url whn parsing xml data, exception says " + e.getMessage());
        }
        return doc;
    }
}
