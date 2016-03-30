package model.databaseManager;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

public class FileSaverTest {

    private String fileName;

    @BeforeMethod
    public void setUp() throws Exception {
        fileName = "exampleDB.txt";
    }

    @Test(groups = "fast")
    public void testCorrectLibraryNameInDB() throws IOException {
        String libraryName = "JBC";
        String content = "example content";
        new FileSaver(libraryName,content,fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))){
            reader.readLine();
            String actualLibraryName = reader.readLine();
            assertThat(actualLibraryName).isEqualTo(libraryName);
        }
    }

    @AfterMethod
    public void tearDown() throws Exception {
        new File(fileName).delete();
    }
}
