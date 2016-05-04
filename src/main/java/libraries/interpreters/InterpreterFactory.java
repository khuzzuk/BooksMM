package libraries.interpreters;

public class InterpreterFactory {
    private static final String bookrixTag = "bookrix";
    private static final String freebookshub = "freebookshub";
    private static final String goodreads = "goodreads";
    public static Interpreter getInterpreter(String url){
        if (url.toLowerCase().contains(bookrixTag))
            return new BookrixInterpreter(url);
        else if (url.toLowerCase().contains(freebookshub))
            return new FreeBooksHubInterpreter(url);
        else if (url.toLowerCase().contains(goodreads))
            return new GoodreadsInterpreter(url);
        return null;
    }
}
