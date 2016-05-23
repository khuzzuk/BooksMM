package manager;

import messaging.subscribers.Subscriber;
import messaging.messages.FinishedQueryMessage;
import messaging.subscribers.SubscriptionType;
import databaseManager.DBRW;
import ui.MainWindow;

import java.io.File;

@SubscriptionType(type = FinishedQueryMessage.class)
public class Start implements Subscriber<FinishedQueryMessage>{
    public Start() {
        subscribe();
    }

    @SuppressWarnings("restriction")
	public static void main(String[] args) {
        new Start();
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
