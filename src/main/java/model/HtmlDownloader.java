package model;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class HtmlDownloader implements XMLParser {
    private static Logger logger = Logger.getLogger(HtmlDownloader.class);
    public static String downloadPage(String url)
    {
        try
        {
            URL pageURL = new URL(url);
            StringBuilder text = new StringBuilder();
            Scanner scanner = new Scanner(pageURL.openStream(), "utf-8");
            try {
                while (scanner.hasNextLine()){
                    text.append(scanner.nextLine());
                }
            }
            finally{
                scanner.close();
            }
            return text.toString();
        }
        catch(Exception ex)
        {
            return null;
        }
    }

    /**
     * Object method that will download page content and return it as a xml data.
     * @param url simple String with URL address.
     * @return {@link org.w3c.dom.Document} with data found at URL address.
     */
    public Document getContentFromURL(String url){
        Document document = null;
        try (InputStream stream = new URL(url).openStream()){
            document = getDocument(stream);
        } catch (MalformedURLException e) {
            logger.error("Wrong URL: " + url);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("File process interrupted.");
            e.printStackTrace();
        }
        return document;
    }
}
