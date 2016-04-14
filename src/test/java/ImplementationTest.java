import channels.Task;
import channels.TaskChannel;
import messaging.subscribers.FinishedTaskSubscriber;
import model.databaseManager.DBRW;
import model.libraries.interpreters.BookrixInterpreter;
import model.libraries.interpreters.FreebookshubInterpreter;
import model.libraries.interpreters.GoodreadsInterpreter;
import org.testng.annotations.Test;
import view.QueryMaker;

import java.io.File;

import static org.mockito.Mockito.mock;

public class ImplementationTest {
    @Test(groups = "integration")
    public void testQuery() throws InterruptedException {
        File file = new File("TestDB.xml");
        if (file.exists()) file.delete();
        DBRW.setOutputDBFile(file);
        DBRW.initializeDB();
        QueryMaker query = mock(QueryMaker.class);
        new FinishedTaskSubscriber(query);
        Task task = new Task(new BookrixInterpreter("http://www.bookrix.com/books;sort:1.html"));
        TaskChannel.channel.putTask(task);
        TaskChannel.channel.putTask(new Task(new GoodreadsInterpreter("http://www.goodreads.com/ebooks")));
        TaskChannel.channel.putTask(new Task(new FreebookshubInterpreter("http://www.freebookshub.com/")));
        Thread.sleep(2000);
        file.delete();
    }
}
