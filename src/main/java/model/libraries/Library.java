package model.libraries;

import org.w3c.dom.NodeList;

import javax.persistence.*;
import java.util.*;

/**
 * This class is a container for a result of a query in particular library. Library is specified by
 * library's page name and date. It contains a map with title and list of its tags.
 */
@Entity
@Table(name = "Queries")
public class Library {
    @Id @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "id")
    private int id;

    @OneToMany(cascade = {CascadeType.ALL})
    @ElementCollection(fetch=FetchType.EAGER)
    //@CollectionTable(name = "Titles", joinColumns = @JoinColumn(name = "titlesId"))
    //@MapKeyColumn(name="title")
    @Column  (name = "category")
    private final Map<Title, String> titles;

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
        titles.put(new Title(title), "");
    }

    /**
     * This method will add the title the container with list of provided tags.
     * @param title {@link String} with the title of the book.
     * @param tags {@link String}s with tags describing the title. It can be empty.
     */
    public void add(String title, String... tags){
        if(tags.length == 0)add(title);
        for (String t:tags) {
            titles.put(new Title(title),t);
        }
    }

    public void addAll(NodeList titleList) {
        for (int i=0; i<titleList.getLength(); i++){
            titles.put(new Title(titleList.item(i).getTextContent()), "");
        }
    }
    public void addAll(String... titles){
        for (String t : titles){
            this.titles.put(new Title(t), "");
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
        Collection<String> titles = new ArrayList<>();
        for (Title t: this.titles.keySet()){
            titles.add(t.getTitle());
        }
        return titles;
    }

    public String getTags(String title) {
        return titles.get(title);
    }
}
