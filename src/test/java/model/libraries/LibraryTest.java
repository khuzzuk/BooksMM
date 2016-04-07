package model.libraries;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LibraryTest {

    private Library library;

    @BeforeMethod
    public void setUp() throws Exception {
        library = new Library("name", "date");
    }

    @Test(groups = "integration")
    public void testAddTitle() {
        library.add("title");
        int actualTitlesNumber = library.getTitles().size();
        int expectedNumber = 1;
        assertThat(actualTitlesNumber).isEqualTo(expectedNumber);
    }

    @Test(groups = "integration")
    public void testAddArrayOfTitles() {
        library.addAll("title1","title2");
        int actualTitlesNumber = library.getTitles().size();
        int expectedNumber = 2;
        assertThat(actualTitlesNumber).isEqualTo(expectedNumber);
    }
}
