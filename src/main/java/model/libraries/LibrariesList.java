package model.libraries;

import model.XMLParser;
import model.databaseManager.XMLWriter;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for managing list of urls where later you can perform a query.
 * All information is linked to xml file (by default it is located in execution path with name
 * "libraries.xml". When no file is found, new one will be created.
 */
public class LibrariesList implements XMLParser {
    private static final LibrariesList instance = new LibrariesList();
    private static final Logger logger = Logger.getLogger(LibrariesList.class);
    ListWriter writer;
    Document doc;
    private File libFile = new File("libraries.xml");
    private static final String URL_ELEMENT = "url";
    private static final String NAME_ATTRIBUTE = "name";

    private LibrariesList() {
    }

    public static LibrariesList getInstance() {
        return instance;
    }

    /**
     * This method will alternate an external xml file with links to the libraries.
     * @param libFile {@link File} that don't have to exist.
     */
    public void setLibFile(File libFile) {
        this.libFile = libFile;
    }

    /**
     * This method will return libraries names stored in category element in provided xml file.
     * It may be used for gui related stuff.
     * When called for the first time it will initialize connection to a file. Mind that
     * this operation can throw {@link org.xml.sax.SAXParseException},
     * {@link java.io.IOException} and other {@link org.w3c.dom.DOMException}.
     * @param category {@link Categories} object by which names will be extracted.
     * @return {@link List}&lt;{@link String}&gt; with libraries names.
     */
    public List<String> getLibrariesNames(Categories category){
        if (doc==null) initializeLibraries();
        Element categoryElement = getCategoryElement(category);
        categoryElement.getChildNodes();
        return extractNames(categoryElement);
    }

    /**
     * This method will provide a {@link List}&lt;{@link String}&gt with ulr addresses to
     * libraries in xml file.
     * When called for the first time it will initialize connection to a file. Mind that
     * this operation can throw {@link org.xml.sax.SAXParseException},
     * {@link java.io.IOException} and other {@link org.w3c.dom.DOMException}.
     * @return {@link List}&lt;{@link String}&gt with ulr addresses.
     */
    public List<String> getAddresses(){
        if (doc==null) initializeLibraries();
        return extractAddresses(doc.getDocumentElement());
    }

    /**
     * This method will provide a {@link List}&lt;{@link String}&gt with ulr addresses to
     * libraries in xml file. It will extract only links from category provided in a parameter
     * When called for the first time it will initialize connection to a file. Mind that
     * this operation can throw {@link org.xml.sax.SAXParseException},
     * {@link java.io.IOException} and other {@link org.w3c.dom.DOMException}.
     * @param category {@link Categories} enum with category which can be found in xml file.
     * @return {@link List}&lt;{@link String}&gt; with ulr addresses.
     */
    public List<String> getAddresses(Categories category){
        if (doc==null) initializeLibraries();
        Element categoryElement = getCategoryElement(category);
        return extractAddresses(categoryElement);
    }
    /**
     * This method will return libraries names stored in category element in provided xml file.
     * It may be used for gui related stuff. It will extract only those libraries that have the same
     * name as provided in {@link String} parameter.
     * When called for the first time it will initialize connection to a file. Mind that
     * this operation can throw {@link org.xml.sax.SAXParseException},
     * {@link java.io.IOException} and other {@link org.w3c.dom.DOMException}.
     * @param category {@link Categories} object by which names will be extracted.
     * @return {@link List}&lt;{@link String}&gt; with libraries names.
     */
    public String getLibraryAddress(String name, Categories category){
        if (doc==null) initializeLibraries();
        Element element = getCategoryElement(category);
        NodeList libraries = element.getElementsByTagName(URL_ELEMENT);
        for (int i=0; i<libraries.getLength(); i++){
            if (getNameAttribute(libraries.item(i)).equals(name)){
                return libraries.item(i).getTextContent();
            }
        }
        return null;
    }

    private Element getCategoryElement(Categories category) {
        return  (Element) doc.getElementsByTagName(category.xmlCategory).item(0);
    }
    private String getNameAttribute(Node element){
        return element.getAttributes().getNamedItem(NAME_ATTRIBUTE).getTextContent();
    }

    private List<String> extractAddresses(Element element) {
        NodeList nodes = element.getElementsByTagName(URL_ELEMENT);
        List<String> addresses = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            addresses.add(nodes.item(i).getTextContent());
        }
        return addresses;
    }
    private List<String> extractNames(Element element) {
        NodeList nodes = element.getElementsByTagName(URL_ELEMENT);
        List<String> names = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            names.add(getNameAttribute(nodes.item(i)));
        }
        return names;
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
        writer.writeFile();
        return true;
    }

    void initializeLibraries() {
        writer = new ListWriter();
        if (libFile.exists()) {
            try (InputStream stream = new FileInputStream(libFile)) {
                doc = getDocument(stream);
            } catch (FileNotFoundException e) {
                logger.error("Interrupted file reading at " + libFile.getPath());
                e.printStackTrace();
            } catch (IOException e) {
                logger.error("Cannot close stream with " + libFile.getPath());
                e.printStackTrace();
            }
        } else startDB();
    }

    private void startDB() {
        doc = getDocument();
        Element root = doc.createElement("root");
        doc.appendChild(root);
        Element romance = doc.createElement(Categories.ROMANCE.xmlCategory);
        Element it = doc.createElement(Categories.IT.xmlCategory);
        Element history = doc.createElement(Categories.HISTORY.xmlCategory);
        Element noCategory = doc.createElement(Categories.NO_CATEGORY.xmlCategory);
        root.appendChild(romance);
        root.appendChild(it);
        root.appendChild(history);
        root.appendChild(noCategory);
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
    class ListWriter implements XMLWriter{
        boolean writeFile(){
            return updateDBFile(libFile, doc);
        }
    }
}
