package messaging.messages;

import libraries.Library;

public class WriteToDBMessage implements Message {
    private final Library item;

    public WriteToDBMessage(Library item) {
        this.item = item;
    }

    public Library getItem() {
        return item;
    }
}
