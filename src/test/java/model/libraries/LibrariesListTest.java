package model.libraries;

import org.testng.annotations.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class LibrariesListTest {
    @Test(groups = "fast")
    public void testReadingFile() {
        LibrariesList list = LibrariesList.getInstance();
        list.setLibFile(new File(LibrariesListTest.class.getResource("/exampleLibraries.xml").getFile()));
        int actualAddressesNumber = list.getAddresses().size();
        assertThat(actualAddressesNumber).isGreaterThan(1);
    }
    @Test(groups = "fast")
    public void testReadingFileByCategory() {
        LibrariesList list = LibrariesList.getInstance();
        list.setLibFile(new File(LibrariesListTest.class.getResource("/exampleLibraries.xml").getFile()));
        int actualAddressesNumber = list.getAddresses(LibrariesList.Categories.ROMANCE).size();
        assertThat(actualAddressesNumber).isGreaterThan(0);
    }

    @Test(groups = "integration")
    public void testWritingToFile() {
        File testFile = new File(LibrariesListTest.class.getResource("/testLibrariesFile.xml").getFile());
        LibrariesList list = LibrariesList.getInstance();
        list.setLibFile(testFile);
        list.addLibraryAddress(LibrariesList.Categories.NO_CATEGORY, "a", "a");
        int actualAddressesNumber = list.getAddresses().size();
        int expectedNumber = 1;
        testFile.delete();
        assertThat(actualAddressesNumber).isGreaterThan(expectedNumber);
    }
}
