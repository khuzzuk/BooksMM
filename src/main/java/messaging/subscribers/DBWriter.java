package messaging.subscribers;

import model.databaseManager.DBRW;
import messaging.messages.WriteToDBMessage;

/**
 * This class is a helper Subscriber for {@link model.databaseManager.DBRW} class.
 * Please remember to not instantiate object explicitly, because proper object will be created
 * with {@link DBRW#initializeDB()} method.
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
