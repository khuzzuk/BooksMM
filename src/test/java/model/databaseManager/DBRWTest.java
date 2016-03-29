package model.databaseManager;

import model.libraries.Library;
import org.testng.annotations.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class DBRWTest {

    private File testFile = new File("testDB.xml");
    private String testElementName = "TestElement";

    @Test
    public void testWriteToNewXml() {
        DBRW.initializeDB();
        DBRW.setOutputDBFile(testFile);
        DBRW.write(new Library(testElementName, "1"));
        int librariesInDB = DBRW.getLibraryByName(testElementName).size();
        int expectedNumber = 1;
        assertThat(librariesInDB).isEqualTo(expectedNumber);
    }

    @Test
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
}
