package libraries.interpreters;

import libraries.Library;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InterpreterTest {

    @DataProvider
    public Object[][] exampleLibrariesLink(){
        return new String[][] {{"http://www.bookrix.com/books.html"},
                {InterpreterTest.class.getResource("/FreeBooksHubExample.html").toString()},
                {InterpreterTest.class.getResource("/goodreadsExample.html").toString()}};
    }

    @Test(groups = "fast")
    public void testGettingLibraryObject() {
        Interpreter interpreter = new Interpreter() {
            @Override
            public Library getQuery() {
                return null;
            }
        };
        Library library = interpreter.getLibraryInstance();
        assertThat(library).isNotNull();
    }

    @Test(groups = "fast", dataProvider = "exampleLibrariesLink")
    public void testIfYouCanGetTitlesFromInterpreters(String url){
        Interpreter interpreter = InterpreterFactory.getInterpreter(url);
        Library l = interpreter.getQuery();
        int actualTitlesFounded = l.size();
        assertThat(actualTitlesFounded).isGreaterThan(0);
    }
}
