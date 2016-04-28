package html;


import org.testng.annotations.Test;

import java.net.MalformedURLException;

import static org.assertj.core.api.Assertions.assertThat;

public class HtmlDownloaderTest {
    @Test(groups = "fast")
    public void checkIfDownloadingMechanismWorks() {
        String readStringFromResource = HtmlDownloader.downloadPage(HtmlDownloaderTest.class.getResource("/test.txt").toString());
        String expectedString = "test";
        assertThat(readStringFromResource).isEqualTo(expectedString);
    }
    @Test(groups = "fast")
    public void testDownloadedPageIsNotEqual() {
        String actualString = HtmlDownloader.downloadPage(HtmlDownloaderTest.class.getResource("/test.txt").toString());
        String expectedString = "test-fail";
        assertThat(actualString).isNotEqualTo(expectedString);
    }

    @Test(groups = "slow")
    public void checkIfDownloadedContentIsUnderstandableByHTMLElement() throws MalformedURLException {
        String url = "http://www.bookrix.com/books.html";
        HtmlDownloader downloader = new HtmlDownloader();
        HtmlElement document = downloader.getContentFromHTML(url);
        int numberOfLibraries = document.select("li").size();
        int zero = 0;
        assertThat(numberOfLibraries).isGreaterThan(zero);
    }
}
