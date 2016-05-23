package libraries;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import xmlParsing.XMLParser;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class LibrariesListTest implements XMLParser {
    LibrariesList list = LibrariesList.getInstance();
    Document doc;

    @BeforeTest(groups = "fast")
    public void prepareDocument() throws IOException {
        doc = getDocument(LibrariesListTest.class.getResource("/exampleLibraries.xml").openStream());
    }
    @BeforeMethod(groups = "fast")
    public void setUp() throws Exception {
        File testFile = new File("test.file");
        LibrariesList.ListWriter writer = mock(LibrariesList.ListWriter.class);
        list.setLibFile(testFile);
        list.initializeLibraries();
        list.writer = writer;
        list.doc = doc;
    }

    @Test(groups = "fast")
    public void checkIfLibrariesListContainedAddresses() {
        //when
        int actualAddressesNumber = list.getAddresses().size();
        //then
        assertThat(actualAddressesNumber).isGreaterThan(1);
    }
    @Test(groups = "fast")
    public void checkIfLibrariesListContainedAddressesByCategory() {
        //when
        int actualAddressesNumber = list.getAddresses(LibrariesList.Categories.ROMANCE).size();
        //then
        assertThat(actualAddressesNumber).isGreaterThan(0);
    }
    @Test(groups = "fast")
    public void checkIfLibrariesListContainedAddresseesByNameAndCategory() {
        //given
        String expectedAddress = "http://www.bookrix.com/books;romance,id:56,sort:1.html";
        //when
        String URLAddress = list.getLibraryAddress("bookrix", LibrariesList.Categories.ROMANCE);
        //then
        assertThat(URLAddress).isEqualTo(expectedAddress);
    }

    @Test(groups = "fast")
    public void testIfNamesOfLibrariesAreCountedCorrectly() throws Exception {
        //given
        int expectedNumber = 1;
        //when
        int actualLibrariesNumber = list.getLibrariesNames(LibrariesList.Categories.ROMANCE).size();
        //then
        assertThat(actualLibrariesNumber).isEqualTo(expectedNumber);
    }

    @Test(groups = "fast")
    public void testIfWritingToFileIsCorrect() throws Exception {
        //when
        boolean statement = list.addLibraryAddress(LibrariesList.Categories.ROMANCE, "url", "name");
        //then
        assertThat(statement).isTrue();
    }
}
