package libraries;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BookFinderTest {
    @Test(groups = "fast")
    public void checkIfNameOfTheLibraryIsCorrect() {
        String address = BookFinderTest.class.getResource("/www.example.library.site.com").toExternalForm();
        String pattern = "abc(.+?)abc";
        BookFinder finder = new BookFinder(address,pattern);
        String list = finder.listOfBooks();
        assertThat(list).isEqualTo("as\n");
    }
}
