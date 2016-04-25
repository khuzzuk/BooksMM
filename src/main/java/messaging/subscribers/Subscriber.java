package messaging.subscribers;


import messaging.messages.Message;
import util.NoTypeChooseBySubscriber;

/**
 * When an object implements this interface, it can subscribe to receive a specified
 * object of class that implements {@link Message} interface. Subscription is made
 * through {@link Subscriber#subscribe()} method which adds this object to a
 * subscription list. {@link Class} type parameter becomes the a key for differentiate {@link Subscriber}s.
 * In order to create object that may subscribe to specific message,
 * just implement this interface and override {@link Subscriber#receive(Message)} method. Another requirement is
 * to provide your class with {@link SubscriptionType} annotation in order to actually receive a message.
 * Also you must invoke {@link Subscriber#subscribe()} method before sending a {@link Message} to a channel.
 * {@link Subscriber#receive(Message)} is not default because you need to provide a logic for receiving a messages.
 * When your object needs to receive more than one message type, it is generally better
 * to provide subscribers helper object for every message type that should be received.
 * Otherwise you will be forced to provide some hierarchy to your {@link Message} objects inheritance
 * and check their class in {@link Subscriber#receive(Message)} method.
 *
 * @param <T> extends {@link Message}, object type that will be received by a subscriber.
 *            When you use raw data type, you have to cast {@link Object} to
 *            {@link Message} or class that implements {@link Message} in order
 *            to actually send a message. When you leave it unchecked, {@link Subscriber}
 *            is probe to throw {@link ClassCastException}.
 */
public interface Subscriber<T extends Message> {
    /**
     * Every class that implements this interface have to provide this method. When a
     * {@link Message} object are processed by MessageWorker,
     * this method will be invoked. Every object implementing this interface should provide
     * in this method a logic for handling a message. Mind, that by default every invocation of
     * {@link Subscriber#receive(Message)} method will be processed by one thread. After processing
     * a method, thread will start processing another message. It is then prone to deadlock when you
     * do all the work with a thread started in this method.
     *
     * @param message object that implements a {@link Message}.
     */
    void receive(T message);

    /**
     * In order to subscribe to a message type, just invoke this method,
     * and then any message of that type will be specified in {@link SubscriptionType#type()} will
     * be treated as a key for mapping subscriptions. By default type is {@link Message}, and if left unchecked
     * it may lead to no message receive by your class.
     * Specified type in annotation will be then a sole criteria for invoking a method. Any class
     * that implements {@link Subscriber} will inherit this method. Also when you leave unchecked
     * data type, you may receive any messages that will match lt;? extends {@link Message}gt;
     * criteria (for example raw {@link Message} type), and the you must decide how to
     * differentiate coming messages. To make design simpler, you may create
     * more {@link Subscriber} classes that will handle one listener and have specified generic
     * class for real type of message.
     * @throws NoTypeChooseBySubscriber when you miss {@link SubscriptionType} annotation.
     */
    default void subscribe() {
        if (this.getClass().isAnnotationPresent(SubscriptionType.class)){
            SubscriptionType t = (SubscriptionType) this.getClass().getAnnotations()[0];
            SubscribersList.subscribe(t.type(), this);
        }
        else throw new NoTypeChooseBySubscriber();
    }
}
