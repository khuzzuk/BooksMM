package messaging.subscribers;

import controller.MainWindowController;
import messaging.Subscriber;
import messaging.messages.WriteToDBMessage;
import model.libraries.Library;

import java.util.Collection;

public class WriteToDBUISubscriber implements Subscriber<WriteToDBMessage>{
    MainWindowController controller;

    public WriteToDBUISubscriber(MainWindowController controller) {
        this.controller = controller;
        subscribe(WriteToDBMessage.class);
    }

    @Override
    public void receive(WriteToDBMessage message) {
        Library library = message.getItem();
        StringBuilder builder = new StringBuilder();
        Collection<String> titles = library.getTitles();
        for (String t : titles) {
            builder.append(t);
            builder.append("\n");
        }
        controller.populateTextArea(builder.toString());
    }
}
