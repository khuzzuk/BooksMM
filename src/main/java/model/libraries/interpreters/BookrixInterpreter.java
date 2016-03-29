package model.libraries.interpreters;

import model.libraries.Library;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This class is responsible for computing a {@link org.jsoup.nodes.Document} object with content from Bookrix.
 * It will find all titles in the content and wrap them into the {@link model.libraries.Library}.
 */
public class BookrixInterpreter extends Interpreter {
    String attributeValue, titleAttributeValue;
    public BookrixInterpreter(Document page) {
        super();
        this.page = page;
        setConstants();
    }

    private void setConstants() {
        name = "bookrix";
        attribute = "class";
        attributeValue="item-content";
        titleAttribute = "item-title";
        titleAttributeValue = "word-break";
    }

    @Override
    public Library getQuery(){
        Library library = getLibraryInstace();
        Elements elements = page.getElementsByAttributeValue(attribute, attributeValue);
        for (Element e : elements){
            Elements title = e.getElementsByAttributeValue(attribute, titleAttribute);
            title = title.get(0).getElementsByAttributeValue(attribute, titleAttributeValue);
            library.add(title.get(0).ownText());
        }
        return library;
    }
}
