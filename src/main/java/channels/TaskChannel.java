package channels;

import channels.workers.TaskWorker;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class TaskChannel {
    private static final TaskChannel channel = new TaskChannel();
    private static final BlockingDeque<Task> channelQueue = new LinkedBlockingDeque<>();
    private static TaskWorker worker;
    private TaskChannel() {
    }
    public static void putTask(Task t){
        if (worker==null) initialize();
        channelQueue.offer(t);
    }

    private static void initialize() {
        worker = new TaskWorker();
        worker.activate(3);
    }
    public static Task poll(){
        return channelQueue.poll();
    }
}
