package model.libraries.interpreters;

import model.libraries.BookFinder;
import model.libraries.Library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GoodreadsInterpreter extends Interpreter {
    private static String pattern = getPattern();

    private String url;

    public GoodreadsInterpreter(String url) {
        super();
        this.url = url;
        name = "goodreads";
    }

    private static String getPattern() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(GoodreadsInterpreter.class.getResourceAsStream("/LIBS")))) {
            String line;
            while ((line=reader.readLine())!=null){
                if (line.equals("http://www.goodreads.com/ebooks")){
                    return reader.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
