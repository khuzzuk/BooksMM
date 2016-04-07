package channels;

import messaging.messages.Message;
import org.testng.annotations.Test;

import java.util.concurrent.BlockingDeque;

import static org.mockito.Mockito.*;

public class MessageChannelTest {
    @Test
    public void testAddingMessageToChannel() {
        //given
        BlockingDeque<Message> channel = mock(BlockingDeque.class);
        MessageChannel.channel.channelQueue = channel;
        Message message = new Message() {};
        //when
        MessageChannel.publish(message);
        //then
        verify(channel).add(message);
    }
}
