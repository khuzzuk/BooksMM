package model.libraries;

import model.databaseManager.LibrariesLoader;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AvailableLibrariesTest {

    private LibrariesLoader loader;
    private String libraryName;

    @BeforeMethod
    public void setUp() throws Exception {
        libraryName = "example";
        BookFinder bookFinder = mock(BookFinder.class);
        when(bookFinder.toString()).thenReturn(libraryName);
        ArrayList<BookFinder> list = new ArrayList<>();
        list.add(bookFinder);
        loader = mock(LibrariesLoader.class);
        when(loader.loadLibraries()).thenReturn(list);
    }

    @Test(groups = "integration")
    public void testCorrectBookFinderInLibraries() {
        AvailableLibraries libraries = new AvailableLibraries(loader);
        String actualFinder = libraries.removeAction(libraryName).toString();
        assertThat(actualFinder).isEqualTo(libraryName);
    }
}
