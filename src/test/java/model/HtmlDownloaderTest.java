package model;


import model.databaseManager.DBRW;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import java.net.MalformedURLException;

import static org.assertj.core.api.Assertions.assertThat;

public class HtmlDownloaderTest {
    @Test(groups = "fast")
    public void testDownloadedPageIsEqual() {
        String actualString = HtmlDownloader.downloadPage(HtmlDownloaderTest.class.getResource("/test.txt").toString());
        String expectedString = "test";
        assertThat(actualString).isEqualTo(expectedString);
    }
    @Test(groups = "fast")
    public void testDownloadedPageIsNotEqual() {
        String actualString = HtmlDownloader.downloadPage(HtmlDownloaderTest.class.getResource("/test.txt").toString());
        String expectedString = "test-fail";
        assertThat(actualString).isNotEqualTo(expectedString);
    }

    @Test(groups = "fast")
    public void testDownloadingPageToXML() throws MalformedURLException {
        String url = HtmlDownloaderTest.class.getResource("/test.xml").toString();
        HtmlDownloader downloader = new HtmlDownloader();
        Document document = downloader.getContentFromURL(url);
        int numberOfLibraries = document.getDocumentElement().getElementsByTagName(DBRW.LIBRARY_ELEMENT).getLength();
        int expectedNumber = 1;
        assertThat(numberOfLibraries).isEqualTo(expectedNumber);
    }
    @Test(groups = "slow")
    public void testDownloadingPageToHTML() throws MalformedURLException {
        String url = "http://www.bookrix.com/books.html";
        HtmlDownloader downloader = new HtmlDownloader();
        org.jsoup.nodes.Document document = downloader.getContentFromHTML(url);
        int numberOfLibraries = document.select("li").size();
        int expectedNumber = 0;
        assertThat(numberOfLibraries).isGreaterThan(expectedNumber);
    }
}
