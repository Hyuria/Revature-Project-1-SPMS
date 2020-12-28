package com.revature.data;

import com.revature.beans.Status;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StatusHibernateTest {
    private StatusHibernate statusHibernate = new StatusHibernate();
    private Status status = new Status();

    @Test
    @Order(1)
    public void testAdd(){
        status.setStatus("test");
        Status retStatus = statusHibernate.add(status);
        assertEquals(status, retStatus);
    }

    @Test
    @Order(2)
    public void testGetById(){
        assertEquals(status, statusHibernate.getById(status.getId()));
    }

    @Test
    @Order(3)
    public void testGetAll(){
        assertTrue(statusHibernate.getAll().size() > 0);
    }

    @Test
    @Order(4)
    public void testUpdate(){
        status.setStatus("TEST UPDATE");
        statusHibernate.update(status);
        assertEquals(status, statusHibernate.getById(status.getId()));
    }

    @Test
    @Order(5)
    public void testDelete(){
        statusHibernate.delete(status);
        assertTrue(statusHibernate.getById(status.getId()) == null);
    }
}
