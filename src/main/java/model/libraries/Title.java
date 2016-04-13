package model.libraries;

import javax.persistence.*;

@Entity
public class Title {
    @Id @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private int titleId;
    @Column(name = "bookTitle")
    private String title;
    @ManyToOne
    @JoinColumn(name = "id")
    private Library library;

    public Title(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
