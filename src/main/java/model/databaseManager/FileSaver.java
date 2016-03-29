package model.databaseManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by mmaczka on 22.03.16.
 */
public class FileSaver extends FileHandler {

    public FileSaver(String libraryName, String content, String fileName) throws IOException {
        file = new File(fileName);
        writer = new FileWriter(fileName, true);

        writer.append("--LIBRARY--\n");
        writer.append(libraryName + "\n");
        writer.append("--DATE--\n");
        writer.append(new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime()) + "\n");
        writer.append(content);
        writer.append("--LIBRARY--\n");

        writer.close();

    }
}
