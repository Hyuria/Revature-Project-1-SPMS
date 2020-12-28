package com.revature.beans;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
public class Genre {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String genre;

    public Genre() {
        id = 0;
        genre = "";
    }

    public Genre(String string){
        switch (string){
            case "fantasy":
                id = 1;
                genre = string;
                break;
            case "mystery":
                id = 2;
                genre = string;
                break;
            case "adventure":
                id = 3;
                genre = string;
                break;
            case "action":
                id = 4;
                genre = string;
                break;
            case "romance":
                id = 5;
                genre = string;
                break;
            case "science_fiction":
                id = 6;
                genre = string;
                break;
            case "horror":
                id = 7;
                genre = string;
                break;
            default:
                id = 0;
                genre = "";
                break;
        }
    }

    public Genre(Integer id, String genre) {
        this.id = id;
        this.genre = genre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre1 = (Genre) o;
        return Objects.equals(id, genre1.id) && Objects.equals(genre, genre1.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, genre);
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", genre='" + genre + '\'' +
                '}';
    }
}
