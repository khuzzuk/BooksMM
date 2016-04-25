package libraries.interpreters;

public class InterpreterFactory {
    private static final String bookrixTag = "bookrix";
    private static final String freebookshub = "freebookshub";
    private static final String goodreads = "goodreads";
    public static Interpreter getInterpreter(String url){
        if (url.contains(bookrixTag))
            return new BookrixInterpreter(url);
        else if (url.contains(freebookshub))
            return new FreebookshubInterpreter(url);
        else if (url.contains(goodreads))
            return new GoodreadsInterpreter(url);
        return null;
    }
}
