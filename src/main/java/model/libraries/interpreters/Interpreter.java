package model.libraries.interpreters;

import model.libraries.Library;
import org.jsoup.nodes.Document;

import java.util.Date;

public abstract class Interpreter {
    protected static String name, attribute, titleAttribute, tagAttribute;
    protected String date;
    protected Document page;

    public Interpreter() {
        date = setDate();
    }

    static String setDate() {
        Date d = new Date(System.currentTimeMillis());
        return d.getYear() + "/" + d.getMonth() + "/" + d.getDay();
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
