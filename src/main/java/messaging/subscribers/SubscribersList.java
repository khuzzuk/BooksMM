package messaging.subscribers;

import messaging.messages.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Here all {@link Subscriber}s objects should be stored. They can
 * subscribe to specific message type. In order to receive a message you must
 * subscribe to {@link Class}&lt;? extends {@link messaging.messages.Message}&gt; object,
 * where ? is actual type of message. Usually {@link Subscriber} has specified
 * a type of message as the checked type in order to receive a message.
 * @since 16.04.16.
 */
public class SubscribersList {
    static SubscribersList list = new SubscribersList();
    static Map<Class<? extends Message>, List<Subscriber<? extends Message>>> subscribers = new HashMap<>();

    private SubscribersList() {}

    @SuppressWarnings("unchecked")
    static void subscribe(Class messageClass, Subscriber<? extends Message> subscriber) {
        List<Subscriber<? extends Message>> list = subscribers.get(messageClass);
        if (list==null) list = new ArrayList<>();
        list.add(subscriber);
        subscribers.put(messageClass, list);
    }

    /**
     * When you invoke this method, you will get a {@link List}&lt;{@link Class}&lt;? extends {@link Message}&gt;&gt;
     * of every subscriber that subscribed for a particular class that extends {@link Message};
     * @param messageClass a {@link Class} that is used as a key for mapping subscribers.
     * @return {@link List}&lt;{@link Class}&lt;? extends {@link Message}&gt;&gt;
     *                  of every subscriber that subscribed for a particular class that extends {@link Message};
     */
    public static List<Subscriber<? extends Message>> get(Class<? extends Message> messageClass) {
        return subscribers.get(messageClass);
    }
}
