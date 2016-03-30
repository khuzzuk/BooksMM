package channels;


import channels.workers.MessageWorker;
import messaging.Subscriber;
import messaging.messages.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;


public class MessageChannel {
    public static final MessageChannel channel = new MessageChannel();
    private final BlockingDeque<Message> channelQueue;
    private final Map<Class<? extends Message>, List<Subscriber<? extends Message>>> subscribers;
    private MessageWorker worker;

    private MessageChannel() {
        channelQueue = new LinkedBlockingDeque<>();
        subscribers = new HashMap<>();
    }

    public void subscribe(Class<? extends Message> messageClass, Subscriber<? extends Message> subscriber) {
        List<Subscriber<? extends Message>> list = subscribers.get(messageClass);
        if (list==null) list = new ArrayList<>();
        list.add(subscriber);
        subscribers.put(messageClass, list);
    }

    public static void publish(Message message) {
        if (channel.worker==null) activateWorker();
        channel.channelQueue.add(message);
    }

    private static void activateWorker() {
        channel.worker = new MessageWorker(channel.channelQueue,channel.subscribers);
        channel.worker.activate();
    }
}
