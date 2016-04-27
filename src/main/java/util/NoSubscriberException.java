package util;

import messaging.subscribers.Subscriber;

/**
 * When there will be bad synchronization with {@link messaging.subscribers.SubscribersList}
 * and {@link channels.MessageChannel}, this exception may be thrown. When you want to find a
 * non listed {@link Subscriber} for a specified {@link Class}&lt;? extends
 * {@link messaging.messages.Message}&gt; in a {@link messaging.subscribers.SubscribersList}.
 * @since 16.04.16.
 */
public class NoSubscriberException extends Exception {
    public NoSubscriberException() {
        super();
    }

    public NoSubscriberException(String s) {
        super(s);
    }
}
