package messaging;

import channels.MessageChannel;
import messaging.messages.Message;

public interface MessageProducer<T extends Message> {
    default void send(T message){
        MessageChannel.publish(message);
    }
}
