package libraries;

import javax.persistence.*;

@Entity
@Table(name = "Titles")
class Title {
    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "titleId")
    private int titleId;

    @Column
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "libID", referencedColumnName = "id")
    private Library library;

    @Column
    private String tag;

    public Title(String title, String tag) {
        this.title = title;
        this.tag = tag;
    }

    public Title(Library library, String tag, String title) {
        this.library = library;
        this.tag = tag;
        this.title = title;
    }

    public Title(String title, Library library) {
        this.title = title;
        this.library = library;
    }

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


}
