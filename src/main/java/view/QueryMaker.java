package view;

import channels.Task;
import channels.TaskChannel;
import messaging.MessageProducer;
import messaging.messages.FinishedQueryMessage;
import messaging.subscribers.FinishedTaskSubscriber;
import model.databaseManager.DBRW;
import model.libraries.LibrariesList;
import model.libraries.interpreters.InterpreterFactory;

import java.util.List;

public class QueryMaker implements MessageProducer<FinishedQueryMessage>{
    private int queryCounter = 0;
    private boolean isInQueryMode;

    public QueryMaker() {
        new FinishedTaskSubscriber(this);
    }

    public void startQuery(){
        startQuery(LibrariesList.Categories.NO_CATEGORY);
    }

    public void startQuery(LibrariesList.Categories category) {
        isInQueryMode=true;
        LibrariesList list = new LibrariesList();
        DBRW.initializeDB();
        List<String> urls = list.getAddresses(category);
        for (String a : urls){
            TaskChannel.putTask(new Task(InterpreterFactory.getInterpreter(a)));
            queryCounter++;
        }
        isInQueryMode=false;
    }
    public void reportFinishedTask(){
        queryCounter--;
        if (!isInQueryMode && queryCounter==0) send(new FinishedQueryMessage());
    }
}
