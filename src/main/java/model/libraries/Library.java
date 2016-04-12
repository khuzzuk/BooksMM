package model.libraries;

import org.w3c.dom.NodeList;

import javax.persistence.*;
import java.util.*;

/**
 * This class is a container for a result of a query in particular library. Library is specified by
 * library's page name and date. It contains a map with title and list of its tags.
 */
@Entity
@Table(name = "Books")
public class Library {
    @Id @GeneratedValue
    @Column(name = "id")
    private int id;
    @javax.persistence.OneToMany(cascade = CascadeType.ALL)
    @javax.persistence.MapKey(name = "title")
    private final Map<String, List<String>> titles;
    @Column(name = "name")
    private final String name;
    @Column(name = "date")
    private final String date;

    /**
     * Constructor creates new instance of a {@link Library} object. It will be an
     * empty container for further query.
     * @param name {@link String} with a name o the library's page.
     * @param date {@link String} with a date of the query in YYYY/MM/DD format.
     */
    public Library(String name, String date) {
        this.name = name;
        this.date = date;
        titles = new HashMap<>();
    }

    /**
     * This method will add a title to a container with empty tag list.
     * @param title {@link String} with a title of the book.
     */
    public void add(String title){
        titles.put(title, new ArrayList<>());
    }

    /**
     * This method will add the title the container with list of provided tags.
     * @param title {@link String} with the title of the book.
     * @param tags {@link String}s with tags describing the title. It can be empty.
     */
    public void add(String title, String... tags){
        List<String> tagList = new ArrayList<>();
        Collections.addAll(tagList, tags);
        titles.put(title, tagList);
    }

    public void addAll(NodeList titleList) {
        for (int i=0; i<titleList.getLength(); i++){
            titles.put(titleList.item(i).getTextContent(), new ArrayList<>());
        }
    }
    public void addAll(String... titles){
        for (String t : titles){
            this.titles.put(t, new ArrayList<>());
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Collection<String> getTitles() {
        return titles.keySet();
    }

    public Collection<String> getTags(String title) {
        return titles.get(title);
    }
}
