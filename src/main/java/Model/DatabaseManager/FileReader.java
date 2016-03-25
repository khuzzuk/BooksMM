package Model.DatabaseManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by mmaczka on 22.03.16.
 */
public class FileReader extends FileHandler {

    String contentNeeded = "";

    public FileReader(String fileName, ArrayList<String> librariesNames) throws IOException {
        file = new File(fileName);
        scanner = new Scanner(file);


        String readPattern = "";
        boolean readNext = true;
        String endLine = "\n";
        boolean checklibrary = false;

        while (scanner.hasNextLine()) {
            readNext = true;

            String line = scanner.nextLine();

            if(checklibrary && !librariesNames.contains(line)) {
                while(scanner.hasNextLine() && !line.equals("--LIBRARY--")) {
                    line = scanner.nextLine();
                }
            }
                checklibrary = false;


            if(line.equals("--DATE--")) {
                readPattern = "Date: ";
                readNext = false;
                endLine = "\n";
            }
            if(line.equals("--LIBRARY--")) {
                checklibrary = true;
                readPattern = "\nLibrary: ";
                readNext = false;
            }
            if(readNext) {
                contentNeeded += readPattern + line + endLine;
                readPattern = "";
                endLine = "\n";
            }


        }

    }

    public String getContentNeeded() {
        return contentNeeded;
    }
}
