package model.libraries;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LibraryTest {

    private Library library;
    private String title;

    @BeforeMethod
    public void setUp() throws Exception {
        library = new Library("name", "date");
        title = "title";
    }

    @Test(groups = "fast")
    public void testAddTitle() {
        library.add(title);
        int actualTitlesNumber = library.getTitles().size();
        int expectedNumber = 1;
        assertThat(actualTitlesNumber).isEqualTo(expectedNumber);
    }

    @Test(groups = "fast")
    public void testAddArrayOfTitles() {
        library.addAll("title1","title2");
        int actualTitlesNumber = library.getTitles().size();
        int expectedNumber = 2;
        assertThat(actualTitlesNumber).isEqualTo(expectedNumber);
    }

    @Test(groups = "fast")
    public void testProperTagWritingsToList() throws Exception {
        //given
        String expectedTag = "tag";
        library.add(title,expectedTag);
        //when
        String libraryTag = library.getTags(title);
        //then
        assertThat(libraryTag).isEqualTo(expectedTag);
    }
}
