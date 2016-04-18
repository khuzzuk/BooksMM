package messaging.subscribers;

import controller.MainWindowController;
import messaging.messages.WriteToDBMessage;
import model.libraries.Library;

import java.util.Collection;

/**
 * It is a helper class for {@link MainWindowController} that will receive messages and forward them to
 * actual handler. It is provided in order to provide a possibility of accepting more than one type
 * of {@link messaging.messages.Message} objects by {@link MainWindowController} class.
 */
public class WriteToDBUISubscriber implements Subscriber<WriteToDBMessage>{
    MainWindowController controller;

    /**
     * When you instantiate this class, you should provide a {@link MainWindowController} object that will
     * be cooperating with this helper. It supports fail fast for null arguments.
     * @param controller {@link MainWindowController} object that will be cooperating in receiving a message through this helper class.
     */
    public WriteToDBUISubscriber(MainWindowController controller) {
        if (controller==null) throw new NullPointerException();
        this.controller = controller;
        subscribe(WriteToDBMessage.class);
    }

    /**
     * There object will receive a message from a {@link channels.MessageChannel} queue. It than will be passed as a {@link String}
     * to {@link MainWindowController} object passed as an argument in the constructor. Every invocation of this method has guarantee,
     * that MessageWorker won't use null parameters, so by default there is no need to check for null parameter.
     * @param message object that implements a {@link messaging.messages.Message}. By default it won't be null.
     */
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
