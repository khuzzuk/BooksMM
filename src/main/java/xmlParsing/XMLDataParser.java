package xmlParsing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import libraries.Library;

class XMLDataParser implements XMLWriter {
	private static XMLDataParser inst = new XMLDataParser();
	private static final String LIBRARY_ELEMENT = "Library";
	private static final String LIBRARY_NAME_ELEMENT = "name";
	private static final String LIBRARY_DATE_ELEMENT = "Date";
	private static final String LIBRARY_TITLE_ELEMENT = "Title";
	private static final String LIBRARY_TITLE_VALUE = "TitleText";
	private static final String LIBRARY_TAG_ELEMENT = "tag";
	private static final String LIBRARY_AUTHOR_ELEMENT = "author";

	private XMLFileWriter writer;
	private File dbFile;
	private Document DB;
	private List<Library> libraries = new ArrayList<>();
	private static final Logger logger = Logger.getLogger(XMLDataParser.class);
	
	private XMLDataParser(){}
	static XMLDataParser getInstance(){return inst;}
	void initialize(){
		writer = new XMLFileWriter();
		dbFile = new File("NewDB.xml");
		libraries = new ArrayList<>();
		if (dbFile.exists())
			readDBFile();
		else
			initializeDBFile();
	}
	static void addLibrary(Library library){
		inst.libraries.add(library);
		inst.newXMLDocument();
		inst.createXMLContent();
		inst.updateDBFile(inst.dbFile, inst.DB);
	}
	private void initializeDBFile() {
		writer.createFile(dbFile);
		newXMLDocument();
	}


	private void newXMLDocument() {
		try {
			DB = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			DB.appendChild(DB.createElement("DB"));
		} catch (ParserConfigurationException e) {
			logger.error("Cannot initialize new DB xml document in " + XMLParsingAspect.class.toString());
		}
	}

    private void createXMLContent() {
        Element root = DB.getDocumentElement();
        //DB.appendChild(root);
        for (Library l : libraries) {
            appendLibraryElement(root, l);
        }
    }

    private void appendLibraryElement(Element root, Library l) {
        Element libraryElement = DB.createElement(LIBRARY_NAME_ELEMENT);
        libraryElement.setAttribute(LIBRARY_NAME_ELEMENT, l.getName());
        root.appendChild(libraryElement);
        appendElement(libraryElement, l.getDate(), LIBRARY_DATE_ELEMENT);
        writeTitles(libraryElement, l);
    }

    private void appendElement(Element rootElement, String newElementTextValue, String newElementName) {
        Element newElement = DB.createElement(newElementName);
        newElement.setTextContent(newElementTextValue);
        rootElement.appendChild(newElement);
    }

    private void writeTitles(Element libraryElement, Library library) {
        for (String t : library.getTitles()) {
            writeTitle(libraryElement, t, library);
        }
    }

    private void writeTitle(Element libraryElement, String title, Library library) {
        Element titleElement = DB.createElement(LIBRARY_TITLE_ELEMENT);
        appendElement(titleElement, title, LIBRARY_TITLE_VALUE);
        String tags = library.getTags(title);
        appendElement(titleElement, tags, LIBRARY_TAG_ELEMENT);
        String author = library.getAuthor(title);
        appendElement(titleElement, author, LIBRARY_AUTHOR_ELEMENT);
        libraryElement.appendChild(titleElement);
    }

    private void readDBFile() {
		try (InputStream stream = new FileInputStream(dbFile)) {
			DB = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
			loadDB();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	private void loadDB() {
		NodeList listOfLibraries = DB.getDocumentElement().getElementsByTagName(LIBRARY_ELEMENT);
		for (int i = 0; i < listOfLibraries.getLength(); i++) {
			loadRecord(listOfLibraries, i);
		}
	}

	private void loadRecord(NodeList listOfLibraries, int i) {
		Element lib = (Element) listOfLibraries.item(i);
		String name = lib.getAttribute(LIBRARY_NAME_ELEMENT);
		String date = lib.getElementsByTagName(LIBRARY_DATE_ELEMENT).item(0).getTextContent();
		Library library = new Library(name, date);
		library.addAll(lib.getElementsByTagName(LIBRARY_TITLE_ELEMENT));
		libraries.add(library);
	}

	/**
     * This method provides a way to change destination file of the logs with books information.
     * By default file will be created in execution path (commonly when the execution file is located),
     * and will be named "DB.xml".
     *
     * @param file - new destination file.
     */
    public void setOutputDBFile(File file) {
        dbFile = file;
    }
    public class XMLFileWriter implements XMLWriter {
        public void createFile(File dbFile) {
            try {
                dbFile.createNewFile();
            } catch (IOException e) {
                logger.error("Cannot create new DB.xml file at " + dbFile.getPath());
                e.printStackTrace();
            }
        }
    }
}
