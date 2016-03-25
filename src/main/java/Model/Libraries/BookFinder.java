package Model.Libraries;

import Model.HtmlDownloader;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mmaczka on 21.03.16.
 */
public class BookFinder {

    String url;
    String pattern;
    private Logger logger = Logger.getLogger(BookFinder.class);

    public String listOfBooks() {
        String result = "";

        String site = HtmlDownloader.downloadPage(url);

        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile(pattern)
                .matcher(site);
        while (m.find()) {
            allMatches.add(m.group(1));
        }

        for(String match : allMatches) {
            result += match + "\n";
        }

        return result;
    }

     public BookFinder(String url, String pattern){
         this.url = url;
         this.pattern = pattern;
     }


    @Override
    public String toString(){
        try {
            Matcher m = Pattern.compile("www.(.+?).com")
                    .matcher(url);
            m.find();
            return m.group(1);
        } catch (Exception e) {
            Component frame=new JFrame();
            JOptionPane.showMessageDialog(frame, "Wrong adress");
            logger.error("couldnt convert to string",e);
            return null;
        }

    }

}
