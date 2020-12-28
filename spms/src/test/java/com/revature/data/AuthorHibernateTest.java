package com.revature.data;

import com.revature.beans.Author;
import com.revature.beans.User;
import com.revature.exception.NonUniqueUsernameException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorHibernateTest {
    private UserHibernate userHibernate = new UserHibernate();
    private AuthorHibernate authorHibernate = new AuthorHibernate();
    private Author author = new Author();
    private User user = new User();

    @Test
    @Order(1)
    public void testAdd() throws NonUniqueUsernameException {
        user.setUsername("test");
        user.setPassword("pwd");
        user.setId(userHibernate.add(user).getId());
        author.setFirstName("John");
        author.setLastName("Smith");
        author.setPoints(100);
        author.setUser(user);
        Author retAuthor = authorHibernate.add(author);
        author.setId(retAuthor.getId());
        assertEquals(author,retAuthor);
    }

    @Test
    @Order(2)
    public void testGetById(){
        assertEquals(author, authorHibernate.getById(author.getId()));
    }

    @Test
    @Order(3)
    public void testGetAll(){
        assertTrue(authorHibernate.getAll().size() > 0);
    }

    @Test
    @Order(4)
    public void testUpdate(){
        author.setFirstName("Jane");
        authorHibernate.update(author);
        assertEquals(author, authorHibernate.getById(author.getId()));
    }

    @Test
    @Order(5)
    public void testDelete(){
        authorHibernate.delete(author);
        userHibernate.delete(user);
        assertTrue(authorHibernate.getById(author.getId()) == null);
    }

}
