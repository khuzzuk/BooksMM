package model;


import model.databaseManager.DBRW;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

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

    @Test
    public void testDownloadingPageToXML() throws MalformedURLException {
        String url = HtmlDownloaderTest.class.getResource("/test.xml").toString();
        HtmlDownloader downloader = new HtmlDownloader();
        Document document = downloader.getContentFromURL(url);
        int numberOfLibraries = document.getDocumentElement().getElementsByTagName(DBRW.LIBRARY_ELEMENT).getLength();
        int expectedNumber = 1;
        assertThat(numberOfLibraries).isEqualTo(expectedNumber);
    }
}
