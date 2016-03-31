package model.databaseManager;

import messaging.MessageProducer;
import messaging.messages.FinishedTaskMessage;
import org.apache.log4j.Logger;
import messaging.subscribers.DBWriter;
import model.libraries.Library;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class is responsible for operation on xml file which logs information about found books.
 * It has only static methods, and no instance of this object is provided.
 * It will read and write a file. Also it has Subscriber helper class {@link DBWriter}.
 */
public class DBRW implements MessageProducer<FinishedTaskMessage> {
    private static final DBRW DBRW = new DBRW();
    static Writer writer = new Writer();
    static Reader reader = new Reader();
    public static final String LIBRARY_ELEMENT = "Library";
    private static final String LIBRARY_NAME_ELEMENT = "name";
    private static final String LIBRARY_DATE_ELEMENT = "Date";
    private static final String LIBRARY_TITLE_ELEMENT = "Title";
    private static final String LIBRARY_TITLE_VALUE = "TitleText";
    private static final String LIBRARY_TAG_ELEMENT = "tag";
    static Document DB;
    private static List<Library> libraries;
    private static File dbFile = new File("DB.xml");
    private static final Logger logger = Logger.getLogger(DBRW.class);

    private DBRW() {
    }

    /**
     * This method will initialize reading and writing logs.
     * In order to reset logs just invoke this method again.
     * It will then read a log file and eventually, when no file
     * is found, it will create a new one.
     * Please mind, that you must initialize {@link DBRW} explicitly,
     * since there is no auto-initialization with first use.
     */
    public static void initializeDB(){
        libraries = new ArrayList<>();
        if (!dbFile.exists()) InitializeDBFile();
        else reader.readDBFile();
        new DBWriter();
    }

    private static void InitializeDBFile() {
        try {
            writer.createFile(dbFile);
            DB = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            DB.appendChild(DB.createElement("DB"));
        } catch (ParserConfigurationException e) {
            logger.error("Cannot initialize new DB xml document in " + DBRW.class.toString());
            e.printStackTrace();
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
        writer.updateDBFile(dbFile, DB);
        DBRW.send(new FinishedTaskMessage());
    }

    /**
     * This method will write an {@link org.w3c.dom.Element} object to the log file.
     * Be aware that the {@link org.w3c.dom.Element} will be added to the root element of a document.
     * @param item {@link org.w3c.dom.Element} that will be added.
     */
    public static void write(Element item){
        DB.getDocumentElement().appendChild(item);
        writer.updateDBFile(dbFile, DB);
    }

    private static void createXMLContent() {
        Element root = DB.createElement("DB");
        DB.appendChild(root);
        for (Library l : libraries){
            appendLibraryElement(root, l);
        }
    }

    private static void appendLibraryElement(Element root, Library l) {
        Element libraryElement = DB.createElement(LIBRARY_NAME_ELEMENT);
        libraryElement.setAttribute(LIBRARY_NAME_ELEMENT, l.getName());
        root.appendChild(libraryElement);
        appendElement(libraryElement, l.getDate(), LIBRARY_DATE_ELEMENT);
        writeTitles(libraryElement, l);
    }

    private static void writeTitles(Element libraryElement, Library library) {
        for (String t : library.getTitles()){
            writeTitle(libraryElement, t, library);
        }
    }

    private static void writeTitle(Element libraryElement, String title, Library library) {
        Element titleElement = DB.createElement(LIBRARY_TITLE_ELEMENT);
        appendElement(titleElement, title, LIBRARY_TITLE_VALUE);
        Collection<String> tags = library.getTags(title);
        for (String tag : tags){
            appendElement(titleElement, tag, LIBRARY_TAG_ELEMENT);
        }
        libraryElement.appendChild(titleElement);
    }

    private static void appendElement(Element rootElement, String newElementTextValue, String newElementName) {
        Element newElement = DB.createElement(newElementName);
        newElement.setTextContent(newElementTextValue);
        rootElement.appendChild(newElement);
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

    /**
     * This method extract all entries for expected libraries.
     * Be aware that libraries will be compared by name.
     * @param name name of the library resided in log file.
     * @return {@link List} of {@link Library} objects.
     */
    public static List<Library> getLibraryByName(String name) {
        List<Library> librariesByName = new ArrayList<>();
        if (libraries==null) return librariesByName;
        for (Library l : libraries){
            if (l.getName().equals(name)) librariesByName.add(l);
        }
        return librariesByName;
    }
    static class Writer implements XMLWriter {
        public void createFile(File dbFile) {
            try {
                dbFile.createNewFile();
            } catch (IOException e) {
                logger.error("Cannot create new DB.xml file at " +dbFile.getPath());
                e.printStackTrace();
            }
        }
    }

    static class Reader {

        private void readDBFile() {
            try(InputStream stream = new FileInputStream(dbFile)) {
                DB = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
                loadDB();
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }
        }

        private void loadDB(){
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
    }
}
