import channels.Task;
import channels.TaskChannel;
import model.HtmlDownloader;
import model.databaseManager.DBRW;
import model.libraries.interpreters.BookrixInterpreter;
import model.libraries.interpreters.FreebookshubInterpreter;
import model.libraries.interpreters.GoodreadsInterpreter;
import org.testng.annotations.Test;
import view.DBWriter;

import java.io.File;

public class ImplementationTest {
    @Test
    public void testQuery() throws InterruptedException {
        DBRW.setOutputDBFile(new File("TestDB.xml"));
        DBRW.initializeDB();
        Task task = new Task(new BookrixInterpreter(new HtmlDownloader().getContentFromHTML("http://www.bookrix.com/books;sort:1.html")));
        TaskChannel.putTask(task);
        TaskChannel.putTask(new Task(new GoodreadsInterpreter("http://www.goodreads.com/ebooks")));
        TaskChannel.putTask(new Task(new FreebookshubInterpreter("http://www.freebookshub.com/")));
        Thread.sleep(1000000000);
    }
}
