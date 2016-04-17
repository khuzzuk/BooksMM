package channels.workers;

import messaging.Subscriber;
import messaging.messages.Message;
import messaging.subscribers.SubscribersList;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingDeque;

public class MessageWorker {
    private final BlockingDeque<? extends Message> channel;

    public MessageWorker(BlockingDeque<? extends Message> channel) {
        this.channel =channel;
    }

    /**
     * This method will create one worker in separate thread that will compute communications between
     * objects. Mind that only one worker is active, so every message will be processed in one thread.
     */
    public void activate(){
            new Thread(new Worker()).start();
    }

    private class Worker implements Runnable {
        @Override
        public void run() {
            Message message;
            //noinspection InfiniteLoopStatement
            while (true){
                message = channel.poll();
                if (message==null){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    @SuppressWarnings("unchecked")
                    List<Subscriber<? extends Message>> currentSubscribers = SubscribersList.get(message.getClass());
                    try {
                        if (currentSubscribers==null)
                        throw new NoSuchElementException("No subscriber for type of message: "+message.getClass());
                    } catch (NoSuchElementException e) {
                        e.printStackTrace();
                    }
                    //noinspection ConstantConditions
                    for (Subscriber s : currentSubscribers){
                        //noinspection unchecked
                        s.receive(message);
                    }
                }
            }
        }
    }
}
