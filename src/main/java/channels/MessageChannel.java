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

/**
 * MessageChannel is responsible for storing objects that implements {@link Message} interface.
 * All messages can be retrieved by internal worker with lazy initialization.
 * When first message is offered to a channel, then it will initialize mechanism
 * for processing messages.
 * Messages are ordered in simple fifo queue. and adding message to a channel is
 * synchronized.
 * All tasks in message channel are designed to be
 * handled by one thread. When you offer a message to a channel, you are assured, that
 * one thread will process a task in subscriber method. Also note, that another message
 * will be processed after {@link MessageWorker} will finished its current task.
 * Mind that when you have two {@link Subscriber}s for one {@link Message} type, message
 * will be received by every subscriber.
 * Another concern is that you mey put a message to a channel that will have no subscriber.
 * In that case {@link MessageWorker} supports fail fast and will stop working with . It is due to
 * bad implementation of messaging bus infrastructure.
 */
public class MessageChannel {
    public static final MessageChannel channel = new MessageChannel();
    BlockingDeque<Message> channelQueue;
    private MessageWorker worker;

    private MessageChannel() {
        channelQueue = new LinkedBlockingDeque<>();
    }

    /**
     * Here you may put a {@link Message} to queue. When {@link MessageWorker} will be free
     * it will poll a {@link Message} and process {@link Subscriber#receive(Message)} method.
     * Mind that only one thred is active to process every message. {@link MessageWorker} will
     * continue processing another message after it finish its current task.
     * Also notice that due to lazy initialization when you offer first task to a channel,
     * it will initialize message infrastructure, which could be costly.
     * @param message object that implements a {@link Message} class.
     */
    public static void publish(Message message) {
        if (channel.worker==null) activateWorker();
        channel.channelQueue.add(message);
    }

    private static void activateWorker() {
        channel.worker = new MessageWorker(channel.channelQueue);
        channel.worker.activate();
    }
}
