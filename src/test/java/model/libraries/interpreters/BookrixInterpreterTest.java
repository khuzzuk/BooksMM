package model.libraries.interpreters;

import model.HtmlDownloader;
import model.libraries.Library;
import org.jsoup.nodes.Document;
import org.testng.annotations.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class BookrixInterpreterTest {
    @Test(groups = "slow")
    public void testFoundTitles() {
        String url = "http://www.bookrix.com/books.html";
        Document document = new HtmlDownloader().getContentFromHTML(url);
        BookrixInterpreter interpreter = new BookrixInterpreter(document);
        Library l = interpreter.getQuery();
        int titlesFound = l.getTitles().size();
        assertThat(titlesFound).isGreaterThan(0);
    }
}
