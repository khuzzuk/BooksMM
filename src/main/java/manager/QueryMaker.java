package manager;

import channels.Task;
import channels.TaskChannel;
import messaging.MessageProducer;
import messaging.messages.FinishedQueryMessage;
import messaging.subscribers.FinishedTaskSubscriber;
import databaseManager.DBRW;
import libraries.LibrariesList;
import libraries.interpreters.InterpreterFactory;
import org.apache.log4j.Logger;
import util.QueryInitializationException;

import java.util.List;

public class QueryMaker implements MessageProducer<FinishedQueryMessage>{
    private int queryCounter = 0;
    private boolean isInQueryMode;
    LibrariesList list;
    TaskChannel channel;
    DBRW dbrw;
    QueryInitializer initializator;
    private static final Logger logger = Logger.getLogger(QueryMaker.class);

    public QueryMaker() {
        new FinishedTaskSubscriber(this);
        initializator = new QueryInitializer();
    }

    public void startQuery(){
        try {
            startQuery(LibrariesList.Categories.NO_CATEGORY);
        } catch (QueryInitializationException e) {
            logger.error("Failed to initialize query. Error in QueryMaker Class.");
            e.printStackTrace();
        }
    }

    /**
     * This method will start a query for specific categories in Libraries List xml file.
     * Throws {@link QueryInitializationException} when initialization file. No operation on file or database will be performed in such a case.
     * @param category {@link libraries.LibrariesList.Categories} that may be found in xml file.
     * @throws QueryInitializationException when lazy initialization fails, you may get this exception. It means, that installation of the program
     * was done wrong in some way.
     */

    public void startQuery(LibrariesList.Categories category) throws QueryInitializationException {
        if (!initializator.initQueries()) throw new QueryInitializationException();
        isInQueryMode=true;
        List<String> urls = list.getAddresses(category);
        for (String a : urls){
            channel.putTask(new Task(InterpreterFactory.getInterpreter(a)));
            queryCounter++;
        }
        isInQueryMode=false;
    }

    /**
     * This method will check if all tasks were finished. If so it will send a proper message to {@link channels.MessageChannel} object.
     */
    public void reportFinishedTask(){
        queryCounter--;
        if (!isInQueryMode && queryCounter==0) send(new FinishedQueryMessage());
    }

    class QueryInitializer {
        boolean initQueries(){
            if (list==null) list = LibrariesList.getInstance();
            if (dbrw==null) {
                dbrw=DBRW.DBRW;
                DBRW.initializeDB();
            }
            if (channel==null) channel=TaskChannel.channel;
            return list!=null && dbrw!=null && channel!=null;
        }
    }
}
