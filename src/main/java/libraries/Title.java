package libraries;

import javax.persistence.*;

@Entity
@Table(name = "Titles")
public class Title {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "titleId")
    private int titleId;

    @Column
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "libID", referencedColumnName = "id")
    private Library library;

    @Column
    private String tag;

    @Column
    private String author;

    /**
     * Just for Hibernate.
     */
    public Title() {
    }

    public Title(String title, String tag) {
        this.title = title;
        this.tag = tag;
    }

    /**
     * Create an object with connected {@link Library}, title and tag.
     *
     * @param library {@link Library}
     * @param tag     tag describing the book.
     * @param title   title of the book.
     */
    public Title(Library library, String tag, String title) {
        this.library = library;
        this.tag = tag;
        this.title = title;
    }

    /**
     * Create an object with connected {@link Library} and title.
     *
     * @param library {@link Library}
     * @param title   title of the book.
     */
    public Title(String title, Library library) {
        this.title = title;
        this.library = library;
    }

    /**
     * Create an object with title.
     *
     * @param title   title of the book.
     */
    public Title(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
