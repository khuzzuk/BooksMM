package model.databaseManager;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

public class LibrariesSaverTest {

    private String fileName;
    private String url;

    @BeforeMethod
    public void setUp() throws Exception {
        fileName = "exampleDB";
        url = "example.com";
    }

    @Test(groups = "integration")
    public void testWritingToFile() throws IOException {
        new LibrariesSaver(url,"a", fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
        reader.readLine();
        String actualURL = reader.readLine();
        assertThat(actualURL).isEqualTo(url);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        new File(fileName).delete();
    }
}
