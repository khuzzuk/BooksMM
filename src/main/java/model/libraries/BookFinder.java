package model.libraries;

import model.HtmlDownloader;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookFinder {

    String url;
    String pattern;

    public String listOfBooks() {
        StringBuilder result = new StringBuilder(2048);

        String site = HtmlDownloader.downloadPage(url);

        Matcher m = Pattern.compile(pattern)
                .matcher(site);
        while (m.find()) {
            result.append(m.group(1)+"\n");
        }

        return result.toString();
    }

     public BookFinder(String url, String pattern){
         this.url = url;
         this.pattern = pattern;
     }


    @Override
    public String toString(){
        return url;
    }

}
