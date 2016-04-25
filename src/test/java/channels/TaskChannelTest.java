package channels;

import messaging.subscribers.FinishedTaskSubscriber;
import databaseManager.DBRW;
import libraries.Library;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import manager.QueryMaker;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TaskChannelTest {

    private File file;
    private QueryMaker query;

    @BeforeMethod
    public void setUp() throws Exception {
        file = new File("TestDB.xml");
        DBRW.setOutputDBFile(file);
        query = mock(QueryMaker.class);
    }

    @Test(groups = "integration")
    public void testQueryFlow() throws InterruptedException {
        DBRW.initializeDB();
        new FinishedTaskSubscriber(query);
        Task task = mock(Task.class);
        when(task.getLibrary()).thenReturn(new Library("a", "a"));
        TaskChannel.channel.putTask(task);
        Thread.sleep(100);
        Task currentTask = TaskChannel.poll();
        assertThat(currentTask).isNull();
    }

    @AfterMethod
    public void tearDown() throws Exception {
        file.delete();
    }
}
