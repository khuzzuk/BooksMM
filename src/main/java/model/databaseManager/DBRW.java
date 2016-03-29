package model.databaseManager;

import view.DBWriter;
import model.libraries.Library;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for operation on xml file wich logs information about found books.
 * It has only static methods, and no instance of this object is provided.
 */
public class DBRW {
    private static final DBRW DBRW = new DBRW();
    public static final String LIBRARY_ELEMENT = "Library";
    private static final String LIBRARY_NAME_ELEMENT = "name";
    private static final String LIBRARY_DATE_ELEMENT = "Date";
    private static final String LIBRARY_TITLE_ELEMENT = "Title";
    static Document DB;
    private static List<Library> libraries;
    private static File dbFile = new File("DB.xml");

    private DBRW() {
    }

    static void initializeDB(){
        libraries = new ArrayList<>();
        try(InputStream stream = new FileInputStream(dbFile)) {
            DB = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
            loadDB();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        new DBWriter();
    }
    private static void loadDB(){
        NodeList listOfLibraries = DB.getDocumentElement().getElementsByTagName(LIBRARY_ELEMENT);
        for (int i=0; i<listOfLibraries.getLength(); i++){
            Element lib = (Element) listOfLibraries.item(i);
            String name = lib.getAttribute(LIBRARY_NAME_ELEMENT);
            String date = lib.getElementsByTagName(LIBRARY_DATE_ELEMENT).item(0).getTextContent();
            Library library = new Library(name,date);
            library.addAll(lib.getElementsByTagName(LIBRARY_TITLE_ELEMENT));
            libraries.add(library);
        }
    }

    /**
     * in this method information will be written to the xml file with books logs.
     * @param library - new library query to write to xml log.
     */
    public static void write(Library library) {
        libraries.add(library);
        newXML();
        createXMLContent();
        updateDBFile();
    }
    public static void write(Element item){
        DB.getDocumentElement().appendChild(item);
        updateDBFile();
    }

    private static void createXMLContent() {
        Element root = DB.createElement("DB");
        DB.appendChild(root);
        Element libraryElement, dateElement, title;
        for (Library l : libraries){
            libraryElement = DB.createElement(LIBRARY_NAME_ELEMENT);
            libraryElement.setAttribute(LIBRARY_NAME_ELEMENT, l.getName());
            root.appendChild(libraryElement);
            dateElement = DB.createElement(LIBRARY_DATE_ELEMENT);
            dateElement.setTextContent(l.getDate());
            libraryElement.appendChild(dateElement);
            writeTitles(libraryElement, l);
        }
    }

    private static void writeTitles(Element libraryElement, Library l) {
        Element title;
        for (String t : l.getTitles()){
            title = DB.createElement(LIBRARY_TITLE_ELEMENT);
            title.setTextContent(t);
            libraryElement.appendChild(title);
        }
    }

    private static void newXML() {
        try {
            DB = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method provides a way to change destination file of the logs with books information.
     * By default file will be created in execution path (commonly when the execution file is located),
     * and will be named "DB.xml".
     * @param file - new destination file.
     */
    public static void setOutputDBFile(File file){
        dbFile=file;
    }

    private static void updateDBFile() {
        try (FileOutputStream outputStream = new FileOutputStream(dbFile)){
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(new DOMSource(DB), new StreamResult(outputStream));
        } catch (TransformerException | IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Library> getLibraryByName(String name) {
        List<Library> librariesByName = new ArrayList<>();
        if (libraries==null) return librariesByName;
        for (Library l : libraries){
            if (l.getName().equals(name)) librariesByName.add(l);
        }
        return librariesByName;
    }
}
