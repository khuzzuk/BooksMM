package html;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class HtmlDownloader implements XMLParser {
    private static final Logger logger = Logger.getLogger(HtmlDownloader.class);
    public static String downloadPage(String url) {
        URL pageURL = null;
        try
        {
            pageURL = new URL(url);
        } catch (MalformedURLException e) {
            logger.error("Wrong address: " + url);
            e.printStackTrace();
        }
        return download(pageURL);
    }

    private static String download(URL pageURL) {
        StringBuilder text = new StringBuilder();
        try (Scanner scanner = new Scanner(pageURL.openStream(), "utf-8");) {
            while (scanner.hasNextLine()){
                text.append(scanner.nextLine());
            }
        } catch (IOException e) {
            logger.error("Cannot connect to " + pageURL.getPath());
            e.printStackTrace();
        }
        return text.toString();
    }

    /**
     * Object method that will download page content and return it as a {@link org.jsoup.nodes.Document}
     * object.
     * @param url simple String with URL address.
     * @return {@link org.jsoup.nodes.Document} with data found at URL address.
     */
    public HtmlElement getContentFromHTML(String url){
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            logger.error("Iterruption during reading an url: "+url);
            e.printStackTrace();
        }
        return new HtmlContent(document);
    }
}
