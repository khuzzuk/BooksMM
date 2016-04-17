package util;

import java.util.NoSuchElementException;

/**
 * When there will be bad synchronization with {@link messaging.subscribers.SubscribersList}
 * and {@link channels.MessageChannel}, this exception may be thrown. When you want to find a
 * non listed {@link messaging.Subscriber} for a specified {@link Class}&lt;? extends
 * {@link messaging.messages.Message}&gt; in a {@link messaging.subscribers.SubscribersList}.
 * @since 16.04.16.
 */
public class NoSubscriberException extends NoSuchElementException {
    public NoSubscriberException() {
        super();
    }

    public NoSubscriberException(String s) {
        super(s);
    }
}
