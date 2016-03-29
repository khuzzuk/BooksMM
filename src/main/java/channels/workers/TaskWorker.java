package channels.workers;

import channels.Task;
import channels.TaskChannel;
import messaging.MessageProducer;
import messaging.messages.WriteToDBMessage;
import model.libraries.Library;

import java.util.concurrent.BlockingDeque;

public class TaskWorker {

    public void activate(int i) {
        for (int j = 0; j < i; j++) {
            new Thread(new Worker()).start();
        }
    }
    private class Worker implements Runnable, MessageProducer {
        @Override
        public void run() {
            Task task;
            while (true){
                task = TaskChannel.poll();
                if (task==null) try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                else {
                    Library library = task.getLibrary();
                    new WriteToDBMessage(library);
                }
            }
        }
    }
}
