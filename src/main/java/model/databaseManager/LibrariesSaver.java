package model.databaseManager;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by mmaczka on 22.03.16.
 */
public class LibrariesSaver extends FileHandler {

    public LibrariesSaver(String url, String pattern, String fileName)  {
        file = new File(fileName);
        Logger logger = Logger.getLogger(LibrariesSaver.class);
        try {
            writer = new FileWriter(fileName, true);
            writer.append("--LIBRARY--\n");
            writer.append(url + "\n");
            writer.append(pattern + "\n");
            writer.append("--LIBRARY--\n");

            writer.close();
        } catch (IOException e) {
           logger.fatal("coudlnt find/save to DB",e);
        }




    }
}
