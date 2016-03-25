package Model.DatabaseManager;

import Model.Libraries.BookFinder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by mmaczka on 22.03.16.
 */
public class LibrariesLoader extends FileHandler {

    String contentNeeded = "";
    public ArrayList<BookFinder> listOfLibraries =new ArrayList<BookFinder>();

    public LibrariesLoader(String fileName) throws IOException {
        file = new File(fileName);
        scanner = new Scanner(file);
    }

    public ArrayList<BookFinder> loadLibraries() {

        boolean readNext = true;

        while (scanner.hasNextLine()) {
            readNext = true;

            String line = scanner.nextLine();

            if(line.equals("--LIBRARY--")) {
                readNext = false;
            }
            if(readNext) {
                String line1 = line;
                String line2 = scanner.nextLine();

                listOfLibraries.add(new BookFinder(line1, line2));
            }
        }

        return listOfLibraries;
    }
}
