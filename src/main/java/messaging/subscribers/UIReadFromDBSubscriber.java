package messaging.subscribers;

import libraries.Title;
import messaging.messages.ReadLibraryFromDBMessage;
import ui.controller.MainWindowController;
import libraries.Library;

import java.util.Collection;
import java.util.List;

/**
 * It is a helper class for {@link MainWindowController} that will receive messages and forward them to
 * actual handler. It is here in order to provide a possibility of accepting more than one type
 * of {@link messaging.messages.Message} objects by {@link MainWindowController} class.
 */
@SubscriptionType(type = ReadLibraryFromDBMessage.class)
public class UIReadFromDBSubscriber implements Subscriber<ReadLibraryFromDBMessage>{
    MainWindowController controller;

    /**
     * When you instantiate this class, you should provide a {@link MainWindowController} object that will
     * be cooperating with this helper. It supports fail fast for null arguments.
     * @param controller {@link MainWindowController} object that will be cooperating in receiving a message through this helper class.
     */
    public UIReadFromDBSubscriber(MainWindowController controller) {
        if (controller==null) throw new NullPointerException();
        this.controller = controller;
        subscribe();
    }

    /**
     * There object will receive a message from a {@link channels.MessageChannel} queue. It than will be passed as a {@link String}
     * to {@link MainWindowController} object passed as an argument in the constructor. Every invocation of this method has guarantee,
     * that MessageWorker won't use null parameters, so by default there is no need to check for null parameter.
     * @param message object that implements a {@link messaging.messages.Message}. By default it won't be null.
     */
    @Override
    public void receive(ReadLibraryFromDBMessage message) {
        List<Library> libraries = message.getLibraries();
        StringBuilder builder = new StringBuilder();
        for (Library l : libraries)
            appendRecords(builder, l);
        controller.populateTextArea(builder.toString());
    }

    private void appendRecords(StringBuilder builder, Library library) {
        Collection<Title> titles = library.getQuery();
        for (Title t : titles) {
            builder.append(t.getTitle());
            if (t.hasTags()){
                builder.append(", TAG: ");
                builder.append(t.getTag());
            }
            if (t.hasAuthors()){
                builder.append(", author: ");
                builder.append(t.getAuthor());
            }
            builder.append("\n");
        }
    }
}
