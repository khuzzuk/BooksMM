package messaging.messages;

import messaging.messages.Message;
import messaging.messages.MessageType;

@MessageType
public class QueryFromDBMessage implements Message{
    String libraryName;

    public QueryFromDBMessage(String libraryName) {
        this.libraryName = libraryName;
    }

    public String getLibraryName() {
        return libraryName;
    }
}
