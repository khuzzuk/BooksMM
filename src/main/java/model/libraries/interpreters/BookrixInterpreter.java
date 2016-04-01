package model.libraries.interpreters;

import model.HtmlDownloader;
import model.libraries.Library;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This class is responsible for computing a {@link org.jsoup.nodes.Document} object with content from Bookrix.
 * It will find all titles in the content and wrap them into the {@link model.libraries.Library}.
 */
public class BookrixInterpreter extends Interpreter {
    String attributeValue, titleAttributeValue, tagAttribute, tagAttributeValue;
    String address;

    public BookrixInterpreter(String address) {
        super();
        setConstants();
        this.address = address;
    }

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
        tagAttribute = "class";
        tagAttributeValue = "item-details";
    }

    @Override
    public Library getQuery(){
        page = new HtmlDownloader().getContentFromHTML(address);
        Library library = getLibraryInstace();
        Elements elements = page.getElementsByAttributeValue(attribute, attributeValue);
        for (Element e : elements)
            addTitleToLibrary(library, e);
        return library;
    }

    private void addTitleToLibrary(Library library, Element e) {
        Elements title = e.getElementsByAttributeValue(attribute, titleAttribute);
        Elements titleValue = title.get(0).getElementsByAttributeValue(attribute, titleAttributeValue);
        Elements itemDetails = e.getElementsByAttributeValue(tagAttribute, tagAttributeValue);
        Elements tags = itemDetails.get(0).getAllElements();
        String tag = tags.get(1).ownText();
        library.add(titleValue.get(0).ownText(), tag);
    }
}
