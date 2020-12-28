package com.revature.data;


import com.revature.beans.User;
import com.revature.exception.NonUniqueUsernameException;
import com.revature.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserHibernateTest {
    private UserHibernate userHibernate = new UserHibernate();
    private User testUser = new User();

    @Test
    @Order(1)
    public void testAdd() throws NonUniqueUsernameException {
        testUser.setUsername("test");
        testUser.setPassword("testpwd");
        User retUser = userHibernate.add(testUser);
        testUser.setId(retUser.getId());
        assertEquals(testUser,retUser);
    }

    @Test
    @Order(2)
    public void testGetById(){
        assertEquals(testUser, userHibernate.getById(testUser.getId()));
    }

    @Test
    @Order(3)
    public void testGetByUsername(){
        assertEquals(testUser, userHibernate.getByUsername(testUser.getUsername()));
    }

    @Test
    @Order(4)
    public void testUpdate(){
        testUser.setUsername("test_update");
        userHibernate.update(testUser);
        assertEquals(testUser, userHibernate.getById(testUser.getId()));
    }

    @Test
    @Order(5)
    public void testGetAll(){
        assertTrue(userHibernate.getAll().size() > 0);
    }

    @Test
    @Order(6)
    public void testDelete() {
        userHibernate.delete(testUser);
        assertTrue(userHibernate.getById(testUser.getId()) == null);
    }

}
