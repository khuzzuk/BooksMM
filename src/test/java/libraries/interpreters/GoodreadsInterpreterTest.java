package libraries.interpreters;

import libraries.Library;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GoodreadsInterpreterTest {
    @Test(groups = "fast")
    public void testFindingTitles() {
        String url = GoodreadsInterpreterTest.class.getResource("/goodreadsExample.html").toString();
        GoodreadsInterpreter interpreter = new GoodreadsInterpreter(url);
        Library l = interpreter.getQuery();
        int actualTitlesFound = l.getTitles().size();
        int expectedTitlesNumber = 50;
        assertThat(actualTitlesFound).isEqualTo(actualTitlesFound);
    }
}
