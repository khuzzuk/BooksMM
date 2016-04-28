package channels;

import messaging.subscribers.FinishedTaskSubscriber;
import databaseManager.DBRW;
import libraries.Library;
import org.hamcrest.core.Is;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import manager.QueryMaker;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TaskChannelTest {

    private File file;
    private QueryMaker queryManager;

    @BeforeMethod
    public void setUp() throws Exception {
        file = new File("TestDB.xml");
        DBRW.setOutputDBFile(file);
        queryManager = mock(QueryMaker.class);
    }

    @Test(groups = "integration")
    public void testIfQueryFlowIsWorkingProperly() {
        DBRW.initializeDB();
        new FinishedTaskSubscriber(queryManager);
        Task task = mock(Task.class);
        when(task.getLibrary()).thenReturn(new Library("a", "a"));
        TaskChannel.channel.putTask(task);
        await().atMost(150, TimeUnit.MILLISECONDS).until(TaskChannel::currentSize, Is.is(0));
    }

    @AfterMethod
    public void tearDown() throws Exception {
        file.delete();
    }
}
