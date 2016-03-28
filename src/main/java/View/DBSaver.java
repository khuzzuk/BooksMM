package View;

import model.databaseManager.FileSaver;
import model.Libraries.AvailableLibraries;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by mmaczka on 22.03.16.
 */
public class DBSaver {
    public static void main(String[] args)  {

        Logger logger = Logger.getLogger(DBSaver.class);

        AvailableLibraries lib = new AvailableLibraries();

        for(int i = 0; i < lib.listOfLibraries.size(); i++)
            try {
                new FileSaver(lib.stringListForGUI.get(i), lib.listOfLibraries.get(i).listOfBooks(), "/home/mmaczka/IdeaProjects/Books/src/main/resources/DB");
            } catch (IOException e) {
                                                                                                                                                             logger.error("wro                       ng path for Database",e);
            }


    }
}
