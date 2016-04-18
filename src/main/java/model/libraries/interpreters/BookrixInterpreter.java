package model.libraries.interpreters;

import model.HtmlDownloader;
import model.HtmlElement;
import model.libraries.Library;

/**
 * This class is responsible for computing a {@link org.jsoup.nodes.Document} object with content from Bookrix.
 * It will find all titles in the content and wrap them into the {@link model.libraries.Library}.
 */
class BookrixInterpreter extends Interpreter {
    private String attributeValue;
    private String titleAttributeValue;
    private String tagAttribute;
    private String tagAttributeValue;
    private String address;

    public BookrixInterpreter(String address) {
        super();
        setConstants();
        this.address = address;
    }

    public BookrixInterpreter(HtmlElement page) {
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

    public Library getQuery(){
        page = new HtmlDownloader().getContentFromHTML(address);
        Library library = getLibraryInstace();
        HtmlElement elements = page.getElementsByAttribute(attribute, attributeValue);
        for (HtmlElement e : elements)
            addTitleToLibrary(library, e);
        return library;
    }

    private void addTitleToLibrary(Library library, HtmlElement e) {
        HtmlElement title = e.getElementsByAttribute(attribute, titleAttribute);
        HtmlElement titleValue = title.getElementsByAttribute(attribute, titleAttributeValue);
        HtmlElement itemDetails = e.getElementsByAttribute(tagAttribute, tagAttributeValue);
        HtmlElement tags = itemDetails.getAllElements();
        String tag = tags.getText(1);
        library.add(titleValue.getText(0), tag);
    }
}
