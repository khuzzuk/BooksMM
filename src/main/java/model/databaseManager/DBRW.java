package model.databaseManager;

import messaging.MessageProducer;
import messaging.messages.FinishedTaskMessage;
import messaging.subscribers.DBWriter;
import model.libraries.Library;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for operation on xml file which logs information about found books.
 * It has only static methods, and no instance of this object is provided.
 * It will read and write a file. Also it has Subscriber helper class {@link DBWriter}.
 */
public class DBRW implements MessageProducer<FinishedTaskMessage> {
    public static final String LIBRARY_ELEMENT = "Library";
    public static final DBRW DBRW = new DBRW();
    private static final String LIBRARY_NAME_ELEMENT = "name";
    private static final String LIBRARY_DATE_ELEMENT = "Date";
    private static final String LIBRARY_TITLE_ELEMENT = "Title";
    private static final String LIBRARY_TITLE_VALUE = "TitleText";
    private static final String LIBRARY_TAG_ELEMENT = "tag";
    private static final Logger logger = Logger.getLogger(DBRW.class);
    static DAOWriter daoWriter;
    static Writer writer = new Writer();
    static Reader reader = new Reader();
    static MessageSender sender = new MessageSender();
    static Document DB;
    static List<Library> libraries;
    private static File dbFile = new File("DB.xml");


    private DBRW() {
    }

    /**
     * This method will initialize reading and writing logs.
     * In order to reset logs just invoke this method again.
     * It will then read a log file and eventually, when no file
     * is found, it will create a new one.
     * Please mind, that you must initialize {@link DBRW} explicitly,
     * since there is no lazy-initialization with first use.
     */
    public static void initializeDB() {
        daoWriter = new DAOWriter();
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
     * This method will commit {@link Library} record to connected database.
     * Additionally information will be written to the xml file with books logs.
     *
     * @param library - new library query to write to database and xml log.
     */
    public static void write(Library library) {
        libraries.add(library);
        newXML();
        createXMLContent();
        writer.updateDBFile(dbFile, DB);
        daoWriter.commitTransaction(library);
        sender.finishedTask();
    }

    public static void shutDown() {
        daoWriter.close();
    }

    /**
     * This method will write an {@link org.w3c.dom.Element} object to the log file.
     * Be aware that the {@link org.w3c.dom.Element} will be added to the root element of a document.
     *
     * @param item {@link org.w3c.dom.Element} that will be added.
     */
    public static void write(Element item) {
        DB.getDocumentElement().appendChild(item);
        writer.updateDBFile(dbFile, DB);
    }

    private static void createXMLContent() {
        Element root = DB.createElement("DB");
        DB.appendChild(root);
        for (Library l : libraries) {
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
        for (String t : library.getTitles()) {
            writeTitle(libraryElement, t, library);
        }
    }

    private static void writeTitle(Element libraryElement, String title, Library library) {
        Element titleElement = DB.createElement(LIBRARY_TITLE_ELEMENT);
        appendElement(titleElement, title, LIBRARY_TITLE_VALUE);
        String tags = library.getTags(title);
        appendElement(titleElement, tags, LIBRARY_TAG_ELEMENT);
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
     *
     * @param file - new destination file.
     */
    public static void setOutputDBFile(File file) {
        dbFile = file;
    }

    /**
     * This method extract all entries for expected libraries.
     * Be aware that libraries will be compared by name.
     *
     * @param name name of the library resided in log file.
     * @return {@link List} of {@link Library} objects.
     */
    public static List<Library> getLibraryByName(String name) {
        List<Library> librariesByName = new ArrayList<>();
        if (libraries == null) return librariesByName;
        for (Library l : libraries) {
            if (l.getName().equals(name)) librariesByName.add(l);
        }
        return librariesByName;
    }

    /**
     * This class is a delegate for {@link DBRW} writing to xml logs.
     */
    static class Writer implements XMLWriter {
        public void createFile(File dbFile) {
            try {
                dbFile.createNewFile();
            } catch (IOException e) {
                logger.error("Cannot create new DB.xml file at " + dbFile.getPath());
                e.printStackTrace();
            }
        }
    }

    /**
     * This class is a delegate for operations in database. Mind that it has lazy initialization so it will open connection
     * when first transaction begins.
     */
    static class DAOWriter {
        private static SessionFactory factory;
        private static Session session;

        public static SessionFactory getFactory() {
            return factory;
        }

        public void commitTransaction(final Library library) {
            if (session == null) initialize();
            session.beginTransaction();
            session.save(library);
            session.getTransaction().commit();
        }

        private void initialize() {
            factory = new Configuration().configure().buildSessionFactory();
            session = factory.openSession();
        }

        /**
         * This method will close current database connection.
         */
        public void close() {
            factory.close();
            session.close();
            session = null;
        }
    }

    static class Reader {

        private void readDBFile() {
            try (InputStream stream = new FileInputStream(dbFile)) {
                DB = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
                loadDB();
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }
        }

        private void loadDB() {
            NodeList listOfLibraries = DB.getDocumentElement().getElementsByTagName(LIBRARY_ELEMENT);
            for (int i = 0; i < listOfLibraries.getLength(); i++) {
                Element lib = (Element) listOfLibraries.item(i);
                String name = lib.getAttribute(LIBRARY_NAME_ELEMENT);
                String date = lib.getElementsByTagName(LIBRARY_DATE_ELEMENT).item(0).getTextContent();
                Library library = new Library(name, date);
                library.addAll(lib.getElementsByTagName(LIBRARY_TITLE_ELEMENT));
                libraries.add(library);
            }
        }
    }

    static class MessageSender implements MessageProducer<FinishedTaskMessage> {
        private void finishedTask() {
            send(new FinishedTaskMessage());
        }
    }
}
