package messaging;


import messaging.messages.Message;
import messaging.subscribers.SubscribersList;

/**
 * When an object implements this interface, it can subscribe to receiving specified
 * object of class that implements {@link Message} interface. Subscription is made
 * through {@link Subscriber#subscribe(Class)} method which adds this object to a
 * subscription list. In order to create object that may subscribe to specific message,
 * just implement this interface and override {@link Subscriber#receive(Message)} method.
 * It is not default because you need to provide a logic for receiving a messages.
 * When your object needs to receive more than one message type, it is generally better
 * to provide subscribers helper object for every message type that should be received.
 * Otherwise you will be forced to provide some hierarchy to your {@link Message} objects
 * and check their class in {@link Subscriber#receive(Message)} method.
 * @param <T> extends {@link Message}, object type that will be received by a subscriber.
 *           When you use raw data type, you have to cast {@link Object} to
 *           {@link Message} or class that implements {@link Message} in order
 *           to actually send a message. When you leave it unchecked, {@link Subscriber}
 *           is probe to throw {@link ClassCastException}.
 */
public interface Subscriber<T extends Message> {
    /**
     * Every class that implements this interface have to provide this method. When a
     * {@link Message} object are processed by {@link channels.workers.MessageWorker},
     * this method will be invoked. Every object implementing this interface should provide
     * in this method a logic for hamdling a message. Mind, that by default every invocation of
     * {@link Subscriber#receive(Message)} method will be processed by one thread. After processing
     * a method, thread will start processing another message.
     * @param message object that implements a {@link Message}.
     */
    void receive(T message);

    /**
     * In order to subscribe to a message type, just invoke this method with object {@link Class}lt;?
     * extends {@link Message}gt;, and then any message of that type will be received in {@link Subscriber#receive(Message)}
     * method. This object will be then a sole criteria for invoking a method. Any class
     * that implements {@link Subscriber} will inherit this method. When you leave unchecked
     * data type, you may receive any messages that will match lt;? extends {@link Message}gt;
     * criteria (for example raw {@link Message} type), and the you must decide how to
     * differentiate coming messages. To make design simplerK, you may create
     * more {@link Subscriber} classes that will handle one listener.
     * @param messageClass {@link Class}lt;? extends {@link Message}gt;, which allows
     *                                  {@link channels.workers.MessageWorker} to send a message
     *                                  to this subscriber. It needs a type to be mapped, since it
     *                                  is the only criteria for sending a message.
     */
    default void subscribe(Class<? extends Message> messageClass){
        SubscribersList.subscribe(messageClass, this);
    }
}
