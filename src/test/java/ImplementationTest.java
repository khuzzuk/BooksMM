import channels.Task;
import channels.TaskChannel;
import messaging.subscribers.FinishedTaskSubscriber;
import databaseManager.DBRW;
import libraries.interpreters.InterpreterFactory;
import org.testng.annotations.Test;
import manager.QueryMaker;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class ImplementationTest {
    @Test(groups = "integration")
    /**
     * Resolve integration of many objects in the system. It follows the happy path where you
     * put to Query three libraries with no concern about tags and categories. It is slow, and because of that it isn't
     * included in maven test group. The result can be found in TestDB.xml file.
     */
    public void testQuery() throws InterruptedException {
        File file = new File("TestDB.xml");
        DBRW.setOutputDBFile(file);
        DBRW.initializeDB();
        QueryMaker query = mock(QueryMaker.class);
        new FinishedTaskSubscriber(query);

        Task task = new Task(InterpreterFactory.getInterpreter("http://www.bookrix.com/books;sort:1.html"));
        TaskChannel.channel.putTask(task);
        TaskChannel.channel.putTask(new Task(InterpreterFactory.getInterpreter("http://www.goodreads.com/ebooks")));
        TaskChannel.channel.putTask(new Task(InterpreterFactory.getInterpreter("http://www.freebookshub.com/")));
        Thread.sleep(2000);
        assertThat(file).exists();
    }
}
