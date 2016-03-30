package model.databaseManager;

import messaging.subscribers.FinishedTaskSubscriber;
import model.libraries.Library;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import view.QueryMaker;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class DBRWTest {

    private File testFile = new File("testDB.xml");
    private String testElementName = "TestElement";

    @Test(groups = "fast")
    public void testWriteToNewXml() {
        QueryMaker query = mock(QueryMaker.class);
        new FinishedTaskSubscriber(query);
        DBRW.initializeDB();
        DBRW.setOutputDBFile(testFile);
        DBRW.write(new Library(testElementName, "1"));
        int librariesInDB = DBRW.getLibraryByName(testElementName).size();
        int expectedNumber = 1;
        assertThat(librariesInDB).isEqualTo(expectedNumber);
    }

    @Test(groups = "fast")
    public void testWriteItemToXML() {
        DBRW.initializeDB();
        DBRW.setOutputDBFile(testFile);
        Element testElement = DBRW.DB.createElement(testElementName);
        testElement.setTextContent("test");
        DBRW.write(testElement);
        NodeList e = DBRW.DB.getDocumentElement().getElementsByTagName(testElementName);
        int testElementsInFile = e.getLength();
        int expectedCount = 1;
        assertThat(testElementsInFile).isEqualTo(expectedCount);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        testFile.delete();
    }
}
