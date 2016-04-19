package view;

import messaging.subscribers.Subscriber;
import messaging.messages.FinishedQueryMessage;
import messaging.subscribers.SubscriptionType;
import model.databaseManager.DBRW;

import java.io.File;

@SubscriptionType(type = FinishedQueryMessage.class)
public class Start implements Subscriber<FinishedQueryMessage>{
    public Start() {
        subscribe();
    }

    public static void main(String[] args) {
        new Start();
        DBRW.setOutputDBFile(new File("TestDB.xml"));
        if (args.length>0 && args[0].equals("-b")){
            QueryMaker query = new QueryMaker();
            query.startQuery();
        }
        else {
            new Thread(() -> {
                javafx.application.Application.launch(MainWindow.class);
            }).start();
        }
    }

    @Override
    public void receive(FinishedQueryMessage message) {
        DBRW.shutDown();
        System.exit(0);
    }
}
