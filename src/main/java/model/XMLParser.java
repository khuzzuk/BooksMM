package model;

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
     * will return {@link org.w3c.dom.Document} object with proper structure.
     * @param stream {@link java.io.InputStream} object which should contain xml data structure.
     * @return {@link org.w3c.dom.Document} with data provided in stream.
     */
    default Document getDocument(InputStream stream){
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
        } catch (ParserConfigurationException e) {
            logger.error("Failed to provide new instance of Document class object. Examine XMLParser class from source code.");
            e.printStackTrace();
        } catch (SAXException e) {
            logger.error("Found malformed content during xml parsing operation.");
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("Failed to connect with url whn parsing xml data.");
            e.printStackTrace();
        }
        return null;
    }
}
