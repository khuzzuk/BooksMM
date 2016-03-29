package model.databaseManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

class DataBaseTranslator {
    private DataBaseTranslator() throws ParserConfigurationException, IOException, TransformerException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(DataBaseTranslator.class.getResourceAsStream("/DB")));
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element db = doc.createElement("DB");
        doc.appendChild(db);

        String line;
        boolean libraryElement = false, dateElement = false;
        Element libraryQuery = null;
        while ((line = reader.readLine()) != null) {
            if (line.equals("--LIBRARY--")) {
                if (!libraryElement) {
                    line = reader.readLine();
                    libraryQuery = doc.createElement("Library");
                    libraryQuery.setAttribute("name", line);
                    db.appendChild(libraryQuery);
                }
                libraryElement = !libraryElement;
            } else if (libraryElement) {
                if (line.equals("--DATE--")) dateElement = !dateElement;
                else if (dateElement) {
                    //noinspection ConstantConditions
                    dateElement = !dateElement;
                    Element date = doc.createElement("Date");
                    date.setTextContent(line);
                    libraryQuery.appendChild(date);
                } else {
                    Element title = doc.createElement("Title");
                    title.setTextContent(line);
                    libraryQuery.appendChild(title);
                }
            }
        }

        File exportFile = new File("DB.xml");
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        FileOutputStream result = new FileOutputStream(exportFile);
        transformer.transform(new DOMSource(doc), new StreamResult(result));
        result.close();
    }

    public static void main(String[] args) {
        try {
            new DataBaseTranslator();
        } catch (ParserConfigurationException | TransformerException | IOException e) {
            e.printStackTrace();
        }
    }
}
