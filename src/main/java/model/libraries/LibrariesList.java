package model.libraries;

import model.XMLParser;
import model.databaseManager.XMLWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for managing list of urls where later you can perform a query.
 * All information is linked to xml file (by default it is located in execution path with name
 * "libraries.xml". When no file is found, new one will be created.
 */
public class LibrariesList implements XMLWriter, XMLParser {

    private Document doc;
    private File libFile = new File("libraries.xml");
    private static final String URL_ELEMENT = "url";
    private static final String NAME_ATTRIBUTE = "name";

    private void startDB() {
        doc = getDocument();
        Element root = doc.createElement("root");
        doc.appendChild(root);
        Element romance = doc.createElement(Categories.ROMANCE.category);
        Element it = doc.createElement(Categories.IT.category);
        Element history = doc.createElement(Categories.HISTORY.category);
        Element noCategory = doc.createElement(Categories.NO_CATEGORY.category);
        root.appendChild(romance);
        root.appendChild(it);
        root.appendChild(history);
        root.appendChild(noCategory);
        updateDBFile(libFile, doc);
    }

    /**
     * This method will alternate an external xml file with links to the libraries.
     * @param libFile {@link File} that don't have to exist.
     */
    public void setLibFile(File libFile) {
        this.libFile = libFile;
    }

    /**
     * Tihs method will provide a {@link List}&lt;{@link String}&gt with ulr addresses to
     * libraries in xml file.
     * When called for the first time it will initialize connection to a file. Mind that
     * this operation can throw {@link org.xml.sax.SAXParseException},
     * {@link java.io.IOException} and other {@link org.w3c.dom.DOMException}.
     * @return {@link List}&lt;{@link String}&gt with ulr addresses.
     */
    public List<String> getAddresses(){
        if (doc==null) initializeLibraries();
        return extractEntries(doc.getDocumentElement());
    }

    /**
     * Tihs method will provide a {@link List}&lt;{@link String}&gt with ulr addresses to
     * libraries in xml file. It will extract only links from category provided in a parameter
     * When called for the first time it will initialize connection to a file. Mind that
     * this operation can throw {@link org.xml.sax.SAXParseException},
     * {@link java.io.IOException} and other {@link org.w3c.dom.DOMException}.
     * @param category {@link Categories} enum with category which can be found in xml file.
     * @return {@link List}&lt;{@link String}&gt with ulr addresses.
     */
    public List<String> getAddresses(Categories category){
        if (doc==null) initializeLibraries();
        Element categoryElement = (Element) doc.getElementsByTagName(category.category).item(0);
        return extractEntries(categoryElement);
    }

    private List<String> extractEntries(Element element) {
        NodeList nodes = element.getElementsByTagName(URL_ELEMENT);
        List<String> addresses = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            addresses.add(nodes.item(i).getTextContent());
        }
        return addresses;
    }

    /**
     * This method will add address to the xml file by category. Only provided set
     * of categories is permitted. All of them is placed in {@link LibrariesList.Categories}.
     * When called for the first time it will initialize connection to a file. Mind that
     * this operation can throw {@link org.xml.sax.SAXParseException},
     * {@link java.io.IOException} and other {@link org.w3c.dom.DOMException}.
     *
     * @param category a {@link LibrariesList.Categories} enum that will match proper
     *                 category in xml file.
     * @param url      a {@link String} with complete url address (with "http://")
     * @param name     a {@link String} with optional name of the library.
     * @return true when operation is successful.
     */
    public boolean addLibraryAddress(Categories category, String url, String name) {
        if (doc == null) initializeLibraries();
        Element address = doc.createElement(URL_ELEMENT);
        address.setTextContent(url);
        address.setAttribute(NAME_ATTRIBUTE, name);
        Element root = (Element) doc.getElementsByTagName(category.xmlCategory).item(0);
        root.appendChild(address);
        updateDBFile(libFile, doc);
        return true;
    }

    private void initializeLibraries() {
        if (libFile.exists()) {
            try {
                doc = getDocument(new FileInputStream(libFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else startDB();
    }

    /**
     * This enum is a helper for {@link LibrariesList} class and is a map for
     * writing ulr in proper category in xml file with libraries urls.
     */
    public enum Categories {
        ROMANCE("Romance", "Romance"), IT("Information technologies", "IT"), HISTORY("History", "History"), NO_CATEGORY("any category", "noCategory");
        private String category;
        String xmlCategory;

        Categories(String category, String xmlCategory) {
            this.category = category;
            this.xmlCategory = xmlCategory;
        }

        @Override
        public String toString() {
            return category;
        }
    }

    public static void main(String[] args) {
        LibrariesList list = new LibrariesList();
        list.addLibraryAddress(Categories.NO_CATEGORY, "http://www.bookrix.com/books;sort:1.html", "bookrix");
        list.addLibraryAddress(Categories.NO_CATEGORY, "http://www.goodreads.com/ebooks", "goodreads");
        list.addLibraryAddress(Categories.NO_CATEGORY, "http://www.freebookshub.com/", "freebookshub");
        list.addLibraryAddress(Categories.ROMANCE, "http://www.bookrix.com/books;romance,id:56,sort:1.html", "bookrix");
        list.addLibraryAddress(Categories.HISTORY, "http://www.bookrix.com/books;history,id:21,sort:1.html", "bookrix");
        list.addLibraryAddress(Categories.IT, "http://www.bookrix.com/books;technology-engineering,id:48,sort:1.html", "bookrix");
    }
}
