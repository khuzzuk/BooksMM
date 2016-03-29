package model.libraries;

import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Library {
    private final List<String> titles;
    private final String name;
    private final String date;

    public Library(String name, String date) {
        this.name = name;
        this.date = date;
        titles = new ArrayList<>();
    }
    public void add(String title){
        titles.add(title);
    }

    public void addAll(NodeList titleList) {
        for (int i=0; i<titleList.getLength(); i++){
            titles.add(titleList.item(i).getTextContent());
        }
    }
    public void addAll(String... titles){
        this.titles.addAll(Arrays.asList(titles));
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public List<String> getTitles() {
        return titles;
    }
}
