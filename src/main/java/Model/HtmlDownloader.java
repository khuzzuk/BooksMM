package Model;

import java.net.URL;
import java.util.Scanner;

/**
 * Created by mmaczka on 21.03.16.
 */
public class HtmlDownloader {
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
}
