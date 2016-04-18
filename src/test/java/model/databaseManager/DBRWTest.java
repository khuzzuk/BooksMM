package model.databaseManager;

import model.libraries.Library;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class DBRWTest {

    private File testFile;
    private String testElementName;
    private Library testLibrary;
    private DBRW.Writer writer;
    private DBRW.DAOWriter dao;

    @BeforeTest(groups = "fast")
    public void setConstants() throws Exception {
        testFile = new File("testDB.xml");
        testElementName = "TestElement";
        testLibrary = new Library(testElementName, "1");
    }

    @BeforeMethod(groups = "fast")
    public void setUp() throws Exception {
        DBRW.initializeDB();
        writer = mock(DBRW.Writer.class);
        DBRW.writer = writer;
        dao = mock(DBRW.DAOWriter.class);
        DBRW.daoWriter = dao;
        DBRW.MessageSender sender = mock(DBRW.MessageSender.class);
        DBRW.sender = sender;
        DBRW.setOutputDBFile(testFile);
        DBRW.libraries = new ArrayList<>();
    }

    @Test(groups = "fast")
    public void testWriteToNewXml() {
        DBRW.write(testLibrary);
        int librariesInDB = DBRW.getLibraryByName(testElementName).size();
        int expectedNumber = 1;
        assertThat(librariesInDB).isEqualTo(expectedNumber);
        verify(writer, times(1)).updateDBFile(testFile, DBRW.DB);
        verify(dao, times(1)).commitTransaction(testLibrary);
    }

    @Test(groups = "fast")
    public void testWriteItemToXML() {
        Element testElement = DBRW.DB.createElement(testElementName);
        DBRW.write(testElement);
        NodeList e = DBRW.DB.getDocumentElement().getElementsByTagName(testElementName);
        int testElementsInFile = e.getLength();
        int expectedCount = 1;
        assertThat(testElementsInFile).isEqualTo(expectedCount);
        verify(writer, times(1)).updateDBFile(testFile, DBRW.DB);
    }
}
