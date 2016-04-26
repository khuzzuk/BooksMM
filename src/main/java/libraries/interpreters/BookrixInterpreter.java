package libraries.interpreters;

import html.HtmlDownloader;
import html.HtmlElement;
import libraries.Library;
import libraries.Title;
import libraries.TitleFactory;

/**
 * This class is responsible for computing a {@link org.jsoup.nodes.Document} object with content from Bookrix.
 * It will find all titles in the content and wrap them into the {@link libraries.Library}.
 */
class BookrixInterpreter extends Interpreter {
    private String attributeValue;
    private String titleAttributeValue;
    private String tagAttribute;
    private String tagAttributeValue;
    private String address;
    private String authorAttribute;

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
        authorAttribute = "item-author";
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
        HtmlElement author = e.getElementsByAttribute(attribute, authorAttribute);
        String tag = tags.getText(1);
        library.add(createTitle(library,titleValue.getText(0),tag,author.getText(0)));
    }

    private Title createTitle(Library library, String title, String tag, String author){
        Title titleObject = TitleFactory.getInstance()
                .setTitle(title)
                .setLibrary(library)
                .setAuthor(author)
                .setTag(tag)
                .build();
        return titleObject;
    }
}
