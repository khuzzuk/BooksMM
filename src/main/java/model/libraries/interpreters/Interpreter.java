package model.libraries.interpreters;

import model.HtmlElement;
import model.libraries.BookFinder;
import model.libraries.Library;

import java.util.Calendar;
import java.util.GregorianCalendar;

public abstract class Interpreter {
    protected String name;
    protected String attribute;
    protected String titleAttribute;
    protected String date;
    protected HtmlElement page;

    public Interpreter() {
        date = setDate();
    }

    static String setDate() {
        GregorianCalendar c = new GregorianCalendar();
        return (c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * This method should return new {@link model.libraries.Library} object
     * with all titles it could find in the provided sources.
     * @return {@link model.libraries.Library} with titles.
     */
    public abstract Library getQuery();

    /**
     * This method should return new {@link model.libraries.Library} object
     * with all titles it could find in the provided sources.
     * @return {@link model.libraries.Library} with titles.
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
