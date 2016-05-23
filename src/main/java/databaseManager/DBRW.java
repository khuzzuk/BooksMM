package databaseManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import libraries.Library;
import messaging.messages.QueryFromDBMessage;
import messaging.subscribers.DBWriter;
import messaging.subscribers.Subscriber;
import messaging.subscribers.SubscriptionType;
import util.WrongLibraryException;
import xmlParsing.XMLWriter;

/**
 * This class is responsible for operation on database which logs information about found books.
 * It has only static methods, and no instance of this object is provided.
 * It will read and write a database. Also it has Subscriber helper class {@link DBWriter}.
 */
public class DBRW {
    public static final DBRW DBRW = new DBRW();
    private static final Logger logger = Logger.getLogger(DBRW.class);
    private DAOWriter daoWriter;
    private DAOReader daoReader;
    //private MessageSender sender = new MessageSender();
    List<Library> libraries;

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
            DBRW.daoWriter.commitTransaction(library);
        } catch (WrongLibraryException e) {
            logger.error(e.getMessage());
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
        if (date.length!=3) throw new WrongLibraryException("Inappropriate date format in a Library object.");
        int year = Integer.parseInt(date[0]);
        if (year<1900 || year> Calendar.getInstance().get(Calendar.YEAR))
            throw new WrongLibraryException("Inappropriate year in Library Object.");
        return (Integer.parseInt(date[1])<0 || Integer.parseInt(date[2])<0);
    }

    public static void shutDown() {
        DAOInitializer.close();
    }

    /**
     * This method extract all entries for expected libraries.
     * Be aware that libraries will be compared by name.
     *
     * @param name name of the library resided in log file.
     * @return {@link List} of {@link Library} objects.
     */
    @SuppressWarnings("unchecked")
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
        new Thread(() -> {getLibraryByName(libraryName);}).start();
    }

    static List getLibrariesFromDB(){
        if (DBRW.daoReader==null) DBRW.daoReader = new DAOReader();
        return DBRW.daoReader.getLibraries();
    }

    @SubscriptionType(type = QueryFromDBMessage.class)
    public static class QueryFromDBChanneller implements Subscriber<QueryFromDBMessage>{
        public QueryFromDBChanneller() {
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
        @SuppressWarnings("unchecked")
		public List<Library> getLibraries(){
            if (DAOInitializer.session==null) DAOInitializer.initialize();
            return DAOInitializer.session.createQuery("FROM Library").list();
        }
    }
    static class DAOInitializer{
        private static SessionFactory factory;
        private static Session session;
        private static void initialize() {
            factory = new Configuration().configure().buildSessionFactory();
            session = factory.openSession();
        }
        static void changeConfiguration(String pathToXml){
            factory = new Configuration().configure(pathToXml).buildSessionFactory();
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

/*    static class MessageSender implements MessageProducer<FinishedTaskMessage> {
        private void finishedTask() {
            send(new FinishedTaskMessage());
        }
    }*/
}
