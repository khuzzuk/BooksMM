package model.libraries;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class FollowedLibrariesTest {

    private BookFinder finder;

    @BeforeMethod
    public void setUp() throws Exception {
        finder = mock(BookFinder.class);
        when(finder.toString()).thenReturn("example");
    }

    @Test(groups = "fast")
    public void testAddLibrary() {
        int expectedSize = 1;
        FollowedLibraries libraries = new FollowedLibraries();
        libraries.addAction(finder);
        int actualLibrariesSize = libraries.listOfLibraries.size();
        assertThat(actualLibrariesSize).isEqualTo(expectedSize);
        actualLibrariesSize = libraries.stringListForGUI.size();
        assertThat(actualLibrariesSize).isEqualTo(expectedSize);
    }
    @Test(groups = "fast")
    public void testRemoveLibrary() {
        int expectedSize = 0;
        FollowedLibraries libraries = new FollowedLibraries();
        libraries.addAction(finder);
        libraries.removeAction(finder.toString());
        int actualLibrariesSize = libraries.listOfLibraries.size();
        assertThat(actualLibrariesSize).isEqualTo(expectedSize);
    }
}
