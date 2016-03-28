package messaging;


import channels.MessageChannel;
import messaging.messages.Message;

public interface Subscriber<T extends Message> {
    void receive(T message);
    default void subscribe(Class<? extends Message> messageClass){
        MessageChannel.channel.subscribe(messageClass, this);
    }
}
