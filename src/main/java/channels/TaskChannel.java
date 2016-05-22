package channels;

import messaging.MessageProducer;
import messaging.messages.WriteToDBMessage;
import libraries.Library;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * It is a queue of {@link Task} object, which will be processed concurrently by more than one worker. When you put
 * a task to a queue with {@link TaskChannel#putTask(Task)} method, it will be processed. By default there is 0-10 ms latency
 * for task to be processed. Also note, that this class is initialized from static context and have lazy initialization.
 * When you invoke {@link TaskChannel#putTask(Task)} for the first time, class will create all threads with workers and start
 * looking for task to process.
 */
public class TaskChannel {
    /**
     * It contains the only instance of a {@link TaskChannel} object that is in use.
     */
    public static final TaskChannel channel = new TaskChannel();
    private static final BlockingDeque<Task> channelQueue = new LinkedBlockingDeque<>();
    private static TaskWorker worker;
    private TaskChannel() {
    }

    /**
     * Puts a {@link Task} object to a queue that will wait for a first free thread to process it.
     * Mint, that it has lazy initialization, which means when you invoke this method for the first time, you may
     * have some latency due to starting thread with workers.
     * Also note, that parameter should be non null. In order to support fail fast method will throw {@link NullPointerException}
     * when it finds a null argument, even before first initialization.
     * @param t {@link Task} object that can be processed in {@link TaskChannel}.
     */
    public void putTask(Task t){
        if (t==null) throw new NullPointerException();
        if (worker==null) initialize();
        channelQueue.offer(t);
    }

    private static void initialize() {
        worker = new TaskWorker();
        worker.activate(3);
    }

    public static int currentSize(){
        return channelQueue.size();
    }

    static Task poll(){
        return channelQueue.poll();
    }

    private static class TaskWorker {

        void activate(@SuppressWarnings("SameParameterValue") int i) {
            for (int j = 0; j < i; j++) {
                new Thread(new Worker()).start();
            }
        }
        private static class Worker implements Runnable {
            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                Task task;
                //noinspection InfiniteLoopStatement
                while (true){
                    task = TaskChannel.poll();
                    if (task==null) try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    else {
                        task.getLibrary();
                        //send(new WriteToDBMessage(library));
                    }
                }
            }
        }
    }
}
