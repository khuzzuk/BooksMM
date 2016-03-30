package channels;

import model.databaseManager.DBRW;
import model.libraries.Library;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TaskChannelTest {

    private File file;

    @BeforeMethod
    public void setUp() throws Exception {
        file = new File("TestDB.xml");
        DBRW.setOutputDBFile(file);
    }

    @Test(groups = "fast")
    public void testQueryFlow() throws InterruptedException {
        DBRW.initializeDB();
        Task task = mock(Task.class);
        when(task.getLibrary()).thenReturn(new Library("a", "a"));
        TaskChannel.putTask(task);
        Thread.sleep(100);
        Task currentTask = TaskChannel.poll();
        assertThat(currentTask).isNull();
    }

    @AfterMethod
    public void tearDown() throws Exception {
        file.delete();
    }
}
