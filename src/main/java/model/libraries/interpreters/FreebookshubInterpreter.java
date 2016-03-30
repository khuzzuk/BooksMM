package model.libraries.interpreters;

import model.libraries.BookFinder;
import model.libraries.Library;

import java.io.InputStreamReader;
import java.util.Scanner;

public class FreebookshubInterpreter extends Interpreter {
    String url;
    private static String pattern = getPattern();

    public FreebookshubInterpreter(String url) {
        super();
        this.url = url;
        name = "freebookshub";
    }

    private static String getPattern() {
        try (Scanner scanner = new Scanner(new InputStreamReader(GoodreadsInterpreter.class.getResourceAsStream("/LIBS")))) {
            String line;
            while ((line = scanner.nextLine()) != null) {
                if (line.equals("http://www.freebookshub.com/")) {
                    return scanner.nextLine();
                }
            }
        }
        return null;
    }

    @Override
    public Library getQuery() {
        Library library = getLibraryInstace();
        BookFinder finder = new BookFinder(url, pattern);
        String[] titles = finder.listOfBooks().split("\\n");
        library.addAll(titles);
        return library;
    }
}
