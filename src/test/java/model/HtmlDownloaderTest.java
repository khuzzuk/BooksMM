package model;


import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HtmlDownloaderTest {
    @Test
    public void testDownloadedPageIsEqual() {
        String actualString = HtmlDownloader.downloadPage(HtmlDownloaderTest.class.getResource("/test.txt").toString());
        String expectedString = "test";
        assertThat(actualString).isEqualTo(expectedString);
    }
    @Test
    public void testDownloadedPageIsNotEqual() {
        String actualString = HtmlDownloader.downloadPage(HtmlDownloaderTest.class.getResource("/test.txt").toString());
        String expectedString = "test-fail";
        assertThat(actualString).isNotEqualTo(expectedString);
    }
}
