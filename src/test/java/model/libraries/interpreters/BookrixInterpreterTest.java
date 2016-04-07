package model.libraries.interpreters;

import model.libraries.Library;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BookrixInterpreterTest {
    @Test(groups = "slow")
    public void testFoundTitles() {
        String url = "http://www.bookrix.com/books.html";
        BookrixInterpreter interpreter = new BookrixInterpreter(url);
        Library l = interpreter.getQuery();
        int titlesFound = l.getTitles().size();
        assertThat(titlesFound).isGreaterThan(0);
    }
}
