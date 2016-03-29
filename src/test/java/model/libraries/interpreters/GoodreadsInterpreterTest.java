package model.libraries.interpreters;

import model.libraries.Library;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GoodreadsInterpreterTest {
    @Test
    public void testFindingTitles() {
        String url = GoodreadsInterpreterTest.class.getResource("/goodreadsExample.html").toString();
        GoodreadsInterpreter interpreter = new GoodreadsInterpreter(url);
        Library l = interpreter.getQuery();
        int actualTitlesFound = l.getTitles().size();
        int expectedTitlesNumber = 50;
        assertThat(actualTitlesFound).isEqualTo(actualTitlesFound);
    }
}
