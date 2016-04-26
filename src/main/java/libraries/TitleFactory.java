package libraries;

import util.TitleInitializationFailureException;

/**
 * Is responsible for creating {@link Title} objects. Every time you invoke {@link TitleFactory#getInstance()} you get a new object
 * without any values. You have to set at least {@link Library} and title values in order to create a Title. Otherwise it will throw
 * {@link util.TitleInitializationFailureException}.
 * @since 2016/04/26
 */
public class TitleFactory {
    String title, tag, author;
    Library library;
    public static TitleFactory getInstance(){
        return new TitleFactory();
    }

    /**
     * Returns an instance of the {@link Title} object with values set in setters. If you won't provide
     * values for {@link Library} and title, it will throw {@link TitleInitializationFailureException}
     * @return {@link Title} objects with filled values.
     */
    public Title build(){
        if (library==null || title==null) throw new TitleInitializationFailureException();
        Title title;
        if (tag==null) title = new Title(this.title, library);
        else title = new Title(library,tag,this.title);
        if (author!=null) title.setAuthor(author);
        return title;
    }

    public TitleFactory setTitle(String title) {
        this.title = title;
        return this;
    }

    public TitleFactory setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public TitleFactory setAuthor(String author) {
        this.author = author;
        return this;
    }

    public TitleFactory setLibrary(Library library) {
        this.library = library;
        return this;
    }
}
