package messaging.subscribers;

import messaging.messages.Message;
import org.testng.annotations.Test;
import util.NoTypeChooseBySubscriber;

import java.util.Set;

@SubscriptionType
public class SubscriberTest implements Subscriber<Message> {
    @Test(groups = "integration")
    public void properKeyForSubscription() throws Exception {
        //given
        //when
        subscribe();
        //then
        Set subscribers = SubscribersList.subscribers.keySet();
        System.out.println(subscribers);
    }

    @Test(groups = "fast", expectedExceptions = NoTypeChooseBySubscriber.class)
    public void exceptionWhenSubscribingForNoType() throws Exception {
        Subscriber s = m -> {};
        s.subscribe();
    }

    @Override
    public void receive(Message message) {}
}
