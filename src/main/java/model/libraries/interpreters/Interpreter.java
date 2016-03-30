package model.libraries.interpreters;

import model.libraries.Library;
import org.jsoup.nodes.Document;

import java.util.Calendar;
import java.util.GregorianCalendar;

public abstract class Interpreter {
    protected String name, attribute, titleAttribute, tagAttribute;
    protected String date;
    protected Document page;

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
    public Library getLibraryInstace(){
        return new Library(name, date);
    }
}
