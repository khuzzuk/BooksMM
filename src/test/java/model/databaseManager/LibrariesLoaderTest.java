package model.databaseManager;

import Model.DatabaseManager.LibrariesLoader;
import Model.Libraries.BookFinder;
import org.testng.annotations.Test;

import java.io.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LibrariesLoaderTest {
    @Test
    public void testCorrectLibraryLoaded() throws IOException {
        String filePath = LibrariesLoaderTest.class.getResource("/LIBS").getFile();
        LibrariesLoader loader = new LibrariesLoader(filePath);
        List<BookFinder> list = loader.loadLibraries();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
        reader.readLine();
        String expectedFinder = new BookFinder(reader.readLine(), reader.readLine()).toString();
        String actualBookFinder = list.get(0).toString();
        assertThat(actualBookFinder).isEqualTo(expectedFinder);
    }
}
