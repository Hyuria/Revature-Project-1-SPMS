package com.revature.beans;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table
public class Committee {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "genre")
    private Genre genre;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "editor_committee",
            joinColumns = @JoinColumn(name = "committee"),
            inverseJoinColumns = @JoinColumn(name = "editor"))
    Set<Editor> editorSet;

    public Committee() {
        id = 0;
        genre = null;
        editorSet = new HashSet<>();
    }

    public Committee(Integer id, Genre genre, Set<Editor> editorSet) {
        this.id = id;
        this.genre = genre;
        this.editorSet = editorSet;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Set<Editor> getEditorSet() {
        return editorSet;
    }

    public void setEditorSet(Set<Editor> editorSet) {
        this.editorSet = editorSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Committee committee = (Committee) o;
        return Objects.equals(id, committee.id) && Objects.equals(genre, committee.genre) && Objects.equals(editorSet, committee.editorSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, genre, editorSet);
    }

    @Override
    public String toString() {
        return "Committee{" +
                "id=" + id +
                ", genre=" + genre +
                ", editorSet=" + editorSet +
                '}';
    }
}
