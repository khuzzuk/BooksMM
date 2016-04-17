package messaging.subscribers;

import messaging.Subscriber;
import messaging.messages.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Here all {@link messaging.Subscriber}s objects should be stored. They can
 * subscribe to specific message type. In order to receive a message you must
 * subscribe to {@link Class}&lt;? extends {@link messaging.messages.Message}&gt; object,
 * where ? is actual type of message. Usually {@link messaging.Subscriber} has specified
 * a type of message as the checked type in order to receive a message.
 * @since 16.04.16.
 */
public class SubscribersList {
    static SubscribersList list = new SubscribersList();
    static Map<Class<? extends Message>, List<Subscriber<? extends Message>>> subscribers;

    private SubscribersList() {
    }
    public static void subscribe(Class<? extends Message> messageClass, Subscriber<? extends Message> subscriber) {
        List<Subscriber<? extends Message>> list = subscribers.get(messageClass);
        if (list==null) list = new ArrayList<>();
        list.add(subscriber);
        subscribers.put(messageClass, list);
    }

    public static List<Subscriber<? extends Message>> get(Class<? extends Message> messageClass) {
        return subscribers.get(messageClass);
    }
}
