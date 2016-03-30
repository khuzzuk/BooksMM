package model.libraries.interpreters;

import model.HtmlDownloader;

public class InterpreterFactory {
    private static final String bookrixTag = "bookrix";
    private static final String freebookshub = "freebookshub";
    private static final String goodreads = "goodreads";
    public static Interpreter getInterpreter(String url){
        HtmlDownloader downloader = new HtmlDownloader();
        if (url.contains(bookrixTag))
            return new BookrixInterpreter(downloader.getContentFromHTML(url));
        else if (url.contains(freebookshub))
            return new FreebookshubInterpreter(url);
        else if (url.contains(goodreads))
            return new GoodreadsInterpreter(url);
        return null;
    }
}
