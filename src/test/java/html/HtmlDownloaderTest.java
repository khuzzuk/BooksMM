package html;


import org.testng.annotations.Test;

import java.net.MalformedURLException;

import static org.assertj.core.api.Assertions.assertThat;

public class HtmlDownloaderTest {
    /**
     * Checks if downloading mechanism works.
     */
    @Test(groups = "fast")
    public void validateDownloadingMechanism() {
        String readFromResource = HtmlDownloader.downloadPage(HtmlDownloaderTest.class.getResource("/test.txt").toString());
        String test = "test";
        assertThat(readFromResource).isEqualTo(test);
    }
    @Test(groups = "fast")
    public void testDownloadedPageIsNotEqual() {
        String actualString = HtmlDownloader.downloadPage(HtmlDownloaderTest.class.getResource("/test.txt").toString());
        String expectedString = "test-fail";
        assertThat(actualString).isNotEqualTo(expectedString);
    }

    /**
     * Checks if downloaded content contains html structure that is understandable by {@link HtmlElement} objects.
     * @throws MalformedURLException
     */
    @Test(groups = "slow")
    public void checkDownloadedContentInHtml() throws MalformedURLException {
        String url = "http://www.bookrix.com/books.html";
        HtmlDownloader downloader = new HtmlDownloader();
        HtmlElement document = downloader.getContentFromHTML(url);
        int numberOfLibraries = document.select("li").size();
        int expectedNumber = 0;
        assertThat(numberOfLibraries).isGreaterThan(expectedNumber);
    }
}
