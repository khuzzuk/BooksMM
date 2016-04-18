package messaging.subscribers;

import messaging.messages.FinishedTaskMessage;
import view.QueryMaker;

public class FinishedTaskSubscriber implements Subscriber<FinishedTaskMessage>{
    private final QueryMaker query;

    public FinishedTaskSubscriber(QueryMaker query) {
        this.query = query;
        subscribe(FinishedTaskMessage.class);
    }

    @Override
    public void receive(FinishedTaskMessage message) {
        query.reportFinishedTask();
    }
}
