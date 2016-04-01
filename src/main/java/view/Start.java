package view;

import messaging.Subscriber;
import messaging.messages.FinishedQueryMessage;
import model.databaseManager.DBRW;

import java.io.File;


public class Start implements Subscriber<FinishedQueryMessage>{
    public Start() {
        subscribe(FinishedQueryMessage.class);
    }

    public static void main(String[] args) {
        new Start();
        DBRW.setOutputDBFile(new File("TestDB.xml"));
        if (args.length>0 && args[0].equals("-b")){
            QueryMaker query = new QueryMaker();
            query.startQuery();
        }
        else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    javafx.application.Application.launch(MainWindow.class);
                }
            }).start();
        }
    }

    @Override
    public void receive(FinishedQueryMessage message) {
        System.exit(0);
    }
}
