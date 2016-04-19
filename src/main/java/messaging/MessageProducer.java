package messaging;

import channels.MessageChannel;
import messaging.messages.Message;

/**
 * If your object need to send a message to a subscriber, it can implement this interface.
 * With this you get the {@link MessageProducer#send(Message)} method, that is responsible for
 * sending messages to a {@link MessageChannel} queue. This interface won't require any implementation
 * for proper working. If you want to send more then one message type, then choose a {@link Message} class
 * as a generic type, instead of raw interface implementation. It is applicable due to fact, that
 * by default {@link MessageChannel} will accept only objects that implements {@link Message} interface.
 * Also note, that you cant send objects that don't implement this interface.
 * @param <T> object that should implement {@link Message} interface (it is mandatory requirement). Enclosing types
 *           are the same as in {@link MessageChannel}, so generally it is better to not use raw generic type.
 */
public interface MessageProducer<T extends Message> {
    /**
     * When a class need to send a message to a {@link MessageChannel}, just invoke this method and provide it with
     * non null argument of &lt;T extends {@link Message}&gt; class. It can't be null, so it supports fail fast and throw
     * {@link NullPointerException} just as you invoke this method.
     * @param message parameter must be not null in order to work properly.
     */
    default void send(T message){
        if (message==null) throw new NullPointerException();
        MessageChannel.publish(message);
    }
}
