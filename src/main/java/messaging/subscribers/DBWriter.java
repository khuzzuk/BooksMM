package messaging.subscribers;

import databaseManager.DBRW;
import messaging.messages.WriteToDBMessage;

/**
 * This class is a helper Subscriber for {@link databaseManager.DBRW} class.
 * Please remember to not instantiate object explicitly, because proper object will be created
 * with {@link DBRW#initializeDB()} method.
 */
@SubscriptionType(type = WriteToDBMessage.class)
public class DBWriter implements Subscriber<WriteToDBMessage> {
    public DBWriter() {
        subscribe();
    }

    @Override
    public void receive(WriteToDBMessage message) {
        DBRW.write(message.getItem());
    }
}
