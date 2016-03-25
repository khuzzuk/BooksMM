package model.databaseManager;

import Model.DatabaseManager.FileReader;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FileReaderTest {
    @Test
    public void testReadingFromDB() throws IOException {
        String testFile = FileReader.class.getResource("/DBTestFile.txt").getFile();
        ArrayList<String> libraries = new ArrayList<String>();
        libraries.add("bookrix");
        FileReader reader = new FileReader(testFile, libraries);
        String readDB = reader.getContentNeeded();
        String endLine = "\n";
        assertThat(readDB).endsWith(endLine);
    }
}
