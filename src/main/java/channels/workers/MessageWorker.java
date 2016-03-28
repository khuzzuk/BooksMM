package channels.workers;

import messaging.Subscriber;
import messaging.messages.Message;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingDeque;

public class MessageWorker {
    private final BlockingDeque<? extends Message> channel;
    private final Map<Class<? extends Message>, List<Subscriber<? extends Message>>> subscribers;

    public MessageWorker(BlockingDeque<? extends Message> channel, Map<Class<? extends Message>, List<Subscriber<? extends Message>>> subscribers) {
        this.channel =channel;
        this.subscribers = subscribers;
    }
    public void activate (int numberOfWorkers){
        for (int i=0; i<numberOfWorkers || i<30; i++){
            new Thread(new Worker()).start();
        }
    }

    private class Worker implements Runnable {
        @Override
        public void run() {
            Message message;
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
                    List<Subscriber<? extends Message>> currentSubscribers = subscribers.get(message.getClass());
                    try {
                        if (currentSubscribers==null)
                        throw new NoSuchElementException("No subscriber for type of message: "+message.getClass());
                    } catch (NoSuchElementException e) {
                        e.printStackTrace();
                    }
                    for (Subscriber s : currentSubscribers){
                        s.receive(message);
                    }
                }
            }
        }
    }
}
