package model.libraries.interpreters;

import model.libraries.Library;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FreebookshubInterpreterTest {
    @Test
    public void testQuery() {
        String url = FreebookshubInterpreterTest.class.getResource("/FreeBooksHubExample.html").toString();
        FreebookshubInterpreter interpreter = new FreebookshubInterpreter(url);
        Library library = interpreter.getQuery();
        int actualTitlesNumber = library.getTitles().size();
        assertThat(actualTitlesNumber).isGreaterThan(1);
    }
}
