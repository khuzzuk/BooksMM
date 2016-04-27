package libraries.interpreters;

import html.HtmlElement;
import libraries.BookFinder;
import libraries.Library;

import java.util.Calendar;
import java.util.GregorianCalendar;

public abstract class Interpreter {
    String name;
    String attribute;
    String titleAttribute;
    String date;
    HtmlElement page;

    public Interpreter() {
        date = setDate();
    }

    static String setDate() {
        GregorianCalendar c = new GregorianCalendar();
        return (c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * This method should return new {@link libraries.Library} object
     * with all titles it could find in the provided sources.
     * @return {@link libraries.Library} with titles.
     */
    public abstract Library getQuery();

    /**
     * This method should return new {@link libraries.Library} object
     * with all titles it could find in the provided sources.
     * @param url {@link String} with library address.
     * @param pattern {@link String} with pattern for title query.
     * @return {@link libraries.Library} with titles.
     */
    public Library getQuery(String url, String pattern){
        Library library = getLibraryInstace();
        BookFinder finder = new BookFinder(url, pattern);
        String[] titles = finder.listOfBooks().split("\\n");
        library.addAll(titles);
        return library;
    }
    public Library getLibraryInstace(){
        return new Library(name, date);
    }
}
