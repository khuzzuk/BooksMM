import channels.Task;
import channels.TaskChannel;
import model.HtmlDownloader;
import model.databaseManager.DBRW;
import model.libraries.interpreters.BookrixInterpreter;
import model.libraries.interpreters.FreebookshubInterpreter;
import model.libraries.interpreters.GoodreadsInterpreter;
import org.testng.annotations.Test;

import java.io.File;

public class ImplementationTest {
    @Test
    public void testQuery() {
        DBRW.setOutputDBFile(new File("TestDB.xml"));
        Task task = new Task(new BookrixInterpreter(new HtmlDownloader().getContentFromHTML("http://www.bookrix.com/books.html")));
        TaskChannel.putTask(task);
        TaskChannel.putTask(new Task(new GoodreadsInterpreter("http://www.goodreads.com/ebooks")));
        TaskChannel.putTask(new Task(new FreebookshubInterpreter("http://www.freebookshub.com/")));
    }
}
