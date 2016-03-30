package view;

import model.databaseManager.DBRW;
import messaging.Subscriber;
import messaging.messages.WriteToDBMessage;

/**
 * This class is a helper Subscriber for {@link model.databaseManager.DBRW} class.
 */
public class DBWriter implements Subscriber<WriteToDBMessage> {
    public DBWriter() {
        subscribe(WriteToDBMessage.class);
    }

    @Override
    public void receive(WriteToDBMessage message) {
        DBRW.write(message.getItem());
    }
}
