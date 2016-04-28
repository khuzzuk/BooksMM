package databaseManager;

import messaging.MessageProducer;
import messaging.messages.QueryFromDBMessage;
import messaging.messages.FinishedTaskMessage;
import messaging.messages.ReadLibraryFromDBMessage;
import messaging.subscribers.DBWriter;
import libraries.Library;
import messaging.subscribers.Subscriber;
import messaging.subscribers.SubscriptionType;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import util.WrongLibraryException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for operation on xml file which logs information about found books.
 * It has only static methods, and no instance of this object is provided.
 * It will read and write a file. Also it has Subscriber helper class {@link DBWriter}.
 */
public class DBRW implements MessageProducer<FinishedTaskMessage> {
    private static final String LIBRARY_ELEMENT = "Library";
    public static final DBRW DBRW = new DBRW();
    private static final String LIBRARY_NAME_ELEMENT = "name";
    private static final String LIBRARY_DATE_ELEMENT = "Date";
    private static final String LIBRARY_TITLE_ELEMENT = "Title";
    private static final String LIBRARY_TITLE_VALUE = "TitleText";
    private static final String LIBRARY_TAG_ELEMENT = "tag";
    private static final String LIBRARY_AUTHOR_ELEMENT = "author";
    private static final Logger logger = Logger.getLogger(DBRW.class);
    private DAOWriter daoWriter;
    private Writer writer = new Writer();
    private Reader reader = new Reader();
    private MessageSender sender = new MessageSender();
    private QueryFromDBChanneler subscriber = new QueryFromDBChanneler();
    static Document DB;
    List<Library> libraries;
    private File dbFile = new File("DB.xml");
    private static DAOReader daoReader;


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
        DBRW.daoWriter = new DAOWriter();
        DBRW.libraries = new ArrayList<>();
        if (!DBRW.dbFile.exists()) InitializeDBFile();
        else DBRW.reader.readDBFile();
        new DBWriter();
    }

    private static void InitializeDBFile() {
        try {
            DBRW.writer.createFile(DBRW.dbFile);
            DB = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            DB.appendChild(DB.createElement("DB"));
        } catch (ParserConfigurationException e) {
            logger.error("Cannot initialize new DB xml document in " + DBRW.class.toString());
            e.printStackTrace();
        }
    }

    /**
     * Commit {@link Library} record to connected database.
     * Additionally information will be written to the xml file with books logs.
     * It will also validate Library object passed as a parameter.
     *
     * @param library - new library query to write to database and xml log.
     */
    public static boolean write(Library library) {
        try {
            isLibraryValid(library);
            DBRW.libraries.add(library);
            newXML();
            createXMLContent();
            DBRW.writer.updateDBFile(DBRW.dbFile, DB);
            DBRW.daoWriter.commitTransaction(library);
            DBRW.sender.finishedTask();
        } catch (WrongLibraryException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    static boolean isLibraryValid(Library library) throws WrongLibraryException {
        if (library == null) throw new WrongLibraryException("Library was null. Operation failed during writing to database.");
        if (library.getName() == null) throw new WrongLibraryException("Library has no name. Operation failed during writing to database.");
        if (library.getDate() == null) throw new WrongLibraryException("Library has no date set. Operation failed during writing to database.");
        if (library.size() == 0) throw new WrongLibraryException("Library has no titles to log. Operation failed during writing to database.");
        String[] date = library.getDate().split("/");
        if (date.length!=3) throw new WrongLibraryException("Inapropriate date format in a Library object.");
        int year = Integer.parseInt(date[2]);
        if (year<1900 || year> Calendar.getInstance().get(Calendar.YEAR))
            throw new WrongLibraryException("Inapropriate year in Library Object.");
        return true;
    }

    public static void shutDown() {
        DAOInitializer.close();
    }

    private static void createXMLContent() {
        Element root = DB.createElement("DB");
        DB.appendChild(root);
        for (Library l : DBRW.libraries) {
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
        String author = library.getAuthor(title);
        appendElement(titleElement, author, LIBRARY_AUTHOR_ELEMENT);
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
        DBRW.dbFile = file;
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
        if (DBRW.libraries == null) DBRW.libraries = getLibrariesFromDB();
        librariesByName.addAll(DBRW.libraries.stream().filter(l -> l.getName().equals(name)).collect(Collectors.toList()));
        return librariesByName;
    }

    /**
     * Will make a query from database and when finished it will send a new {@link QueryFromDBMessage}.
     */
    public static void QueryLibraries(String libraryName){
        new Thread(() -> {DBRW.subscriber.send(new ReadLibraryFromDBMessage(getLibraryByName(libraryName)));}).start();
    }

    static List getLibrariesFromDB(){
        if (daoReader==null) daoReader = new DAOReader();
        return daoReader.getLibraries();
    }

    @SubscriptionType(type = QueryFromDBMessage.class)
    public static class QueryFromDBChanneler implements Subscriber<QueryFromDBMessage>, MessageProducer<ReadLibraryFromDBMessage> {
        public QueryFromDBChanneler() {
            subscribe();
        }

        @Override
        public void receive(QueryFromDBMessage message) {
            DBRW.QueryLibraries(message.getLibraryName());
        }
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

        public void commitTransaction(final Library library) {
            if (DAOInitializer.session == null) DAOInitializer.initialize();
            DAOInitializer.session.beginTransaction();
            DAOInitializer.session.save(library);
            DAOInitializer.session.getTransaction().commit();
        }


    }
    static class DAOReader {
        public List getLibraries(){
            if (DAOInitializer.session==null) DAOInitializer.initialize();
            Transaction tx = DAOInitializer.session.beginTransaction();
            List libraries = DAOInitializer.session.createQuery("FROM Library").list();
            return libraries;
        }
    }
    static class DAOInitializer{
        private static SessionFactory factory;
        private static Session session;
        private static void initialize() {
            factory = new Configuration().configure().buildSessionFactory();
            session = factory.openSession();
        }
        /**
         * This method will close current database connection.
         */
        private static void close() {
            factory.close();
            session = null;
        }
    }

    static class Reader {

        private void readDBFile() {
            try (InputStream stream = new FileInputStream(DBRW.dbFile)) {
                DB = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
                loadDB();
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }
        }

        private void loadDB() {
            NodeList listOfLibraries = DB.getDocumentElement().getElementsByTagName(LIBRARY_ELEMENT);
            for (int i = 0; i < listOfLibraries.getLength(); i++) {
                loadRecord(listOfLibraries, i);
            }
        }

        private void loadRecord(NodeList listOfLibraries, int i) {
            Element lib = (Element) listOfLibraries.item(i);
            String name = lib.getAttribute(LIBRARY_NAME_ELEMENT);
            String date = lib.getElementsByTagName(LIBRARY_DATE_ELEMENT).item(0).getTextContent();
            Library library = new Library(name, date);
            library.addAll(lib.getElementsByTagName(LIBRARY_TITLE_ELEMENT));
            DBRW.libraries.add(library);
        }
    }

    static class MessageSender implements MessageProducer<FinishedTaskMessage> {
        private void finishedTask() {
            send(new FinishedTaskMessage());
        }
    }
}
