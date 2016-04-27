package libraries;

import org.w3c.dom.NodeList;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is a container for a result of a query in particular library. Library is specified by
 * library's page name and date. It contains a map with title and list of its tags.
 */
@Entity
@Table(name = "Queries")
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @OneToMany(cascade = CascadeType.ALL)
    private final List<Title> titles;

    @Column(name = "name")
    private final String name;
    @Column(name = "date")
    private final String date;


    /**
     * Constructor creates new instance of a {@link Library} object. It will be an
     * empty container for further query.
     *
     * @param name {@link String} with a name o the library's page.
     * @param date {@link String} with a date of the query in YYYY/MM/DD format.
     */
    public Library(String name, String date) {
        this.name = name;
        this.date = date;
        titles = new ArrayList<>();
    }

    /**
     * This constructor is for Hibernate compatibility.
     */
    public Library() {
        titles = new ArrayList<>();
        name = null;
        date = null;
    }

    /**
     * This method will add a title to a container with empty tag list.
     *
     * @param title {@link String} with a title of the book.
     */
    public void add(String title) {
        titles.add(new Title(title,this));
    }

    /**
     * This method will add the title the container with list of provided tags.
     *
     * @param title {@link String} with the title of the book.
     * @param tags  {@link String}s with tags describing the title. It can be empty.
     */
    public void add(String title, String... tags) {
        if (tags.length == 0) add(title);
        for (String t : tags) {
            titles.add(new Title(this,t, title));
        }
    }

    /**
     * Will add a {@link Title} object to a {@link Library} for further operations.
     * @param title {@link Title} object.
     */
    public void add(Title title){
        titles.add(title);
    }

    /**
     * This method handles adding elements in {@link NodeList} provided with parameters.
     * @param titleList {@link NodeList} with libraries elements.
     * @deprecated
     */
    @Deprecated
    public void addAll(NodeList titleList) {
        for (int i = 0; i < titleList.getLength(); i++) {
            titles.add(new Title(titleList.item(i).getTextContent(),this));
        }
    }

    public void addAll(String... titles) {
        for (String t : titles) {
            this.titles.add(new Title(t,this));
        }
    }

    @SuppressWarnings("unused")
    public int getId() {
        return id;
    }

    @SuppressWarnings("unused")
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
        return titles.stream().map(Title::getTitle).collect(Collectors.toCollection(ArrayList::new));
    }

    public String getTags(String title) {
        String titleFromList;
        for (Title t : titles) {
            titleFromList = t.getTitle();
            if (titleFromList.equals(title))
                return t.getTag();
        }
        return "";
    }

    public String getAuthor(String title) {
        String titleFromList;
        for (Title t : titles) {
            titleFromList = t.getTitle();
            if (titleFromList.equals(title))
                return t.getAuthor();
        }
        return "";
    }

    public int size(){
        return titles.size();
    }
}
