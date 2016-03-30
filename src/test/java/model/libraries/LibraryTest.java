package model.libraries;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LibraryTest {

    private Library l;

    @BeforeMethod
    public void setUp() throws Exception {
        l = new Library("name", "date");
    }

    @Test(groups = "fast")
    public void testAddTitle() {
        l.add("title");
        int actualTitlesNumber = l.getTitles().size();
        int expectedNumber = 1;
        assertThat(actualTitlesNumber).isEqualTo(expectedNumber);
    }

    @Test(groups = "fast")
    public void testAddArrayOfTitles() {
        l.addAll("title1","title2");
        int actualTitlesNumber = l.getTitles().size();
        int expectedNumber = 2;
        assertThat(actualTitlesNumber).isEqualTo(expectedNumber);
    }
}
