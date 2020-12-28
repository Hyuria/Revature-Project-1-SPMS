package com.revature.data;

import com.revature.beans.Genre;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GenreHibernateTest {
    private GenreHibernate genreHibernate = new GenreHibernate();
    private Genre genre = new Genre();

    @Test
    @Order(1)
    public void testAdd(){
        genre.setGenre("historical");
        Genre retGenre = genreHibernate.add(genre);
        genre.setId(retGenre.getId());
        assertEquals(genre, retGenre);
    }

    @Test
    @Order(2)
    public void testGetById(){
        assertEquals(genre, genreHibernate.getById(genre.getId()));
    }

    @Test
    @Order(3)
    public void testGetAll(){
        assertTrue(genreHibernate.getAll().size() > 0);
    }

    @Test
    @Order(4)
    public void testUpdate(){
        genre.setGenre("drama");
        genreHibernate.update(genre);
        assertEquals(genre, genreHibernate.getById(genre.getId()));
    }

    @Test
    @Order(5)
    public void testDelete(){
        genreHibernate.delete(genre);
        assertTrue(genreHibernate.getById(genre.getId()) == null);
    }
}
