package messaging.subscribers;
import messaging.messages.QueryFromDBMessage;

@SubscriptionType(type = QueryFromDBMessage.class)
    public class QueryFromDBChanneller implements Subscriber<QueryFromDBMessage>{
        public QueryFromDBChanneller() {
            subscribe();
        }

        @Override
        public void receive(QueryFromDBMessage message) {
        }
    }
