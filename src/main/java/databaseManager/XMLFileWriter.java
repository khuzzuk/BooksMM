package databaseManager;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import xmlParsing.XMLWriter;

public class XMLFileWriter implements XMLWriter {
	private static final Logger logger = Logger.getLogger(XMLFileWriter.class);
    public void createFile(File dbFile) {
        try {
            dbFile.createNewFile();
        } catch (IOException e) {
            logger.error("Cannot create new DB.xml file at " + dbFile.getPath());
            e.printStackTrace();
        }
    }
}