package View;

import model.databaseManager.DBRW;
import messaging.Subscriber;
import messaging.messages.WriteToDBMessage;

public class DBWriter implements Subscriber<WriteToDBMessage> {
    public DBWriter() {
        subscribe(WriteToDBMessage.class);
    }

    @Override
    public void receive(WriteToDBMessage message) {
        DBRW.write(message.getItem());
    }
}
