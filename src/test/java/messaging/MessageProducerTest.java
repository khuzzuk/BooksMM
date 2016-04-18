package messaging;

import messaging.messages.Message;
import org.testng.annotations.Test;

public class MessageProducerTest {
    @Test(groups = "fast", expectedExceptions = NullPointerException.class)
    public void checkNullException() throws Exception {
        //given
        MessageProducer producer = new MessageProducer() {};
        //when
        producer.send(null);
        //then
    }
}
