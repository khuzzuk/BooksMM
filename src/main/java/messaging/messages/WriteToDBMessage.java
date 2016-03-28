package messaging.messages;

import model.Libraries.Library;
import org.w3c.dom.Element;

public class WriteToDBMessage implements Message {
    Library item;

    public WriteToDBMessage(Library item) {
        this.item = item;
    }

    public Library getItem() {
        return item;
    }
}
