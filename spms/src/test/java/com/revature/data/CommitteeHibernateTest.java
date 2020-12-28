package com.revature.data;


import com.revature.beans.Committee;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommitteeHibernateTest {
    private CommitteeHibernate committeeHibernate = new CommitteeHibernate();
    private GenreHibernate genreHibernate = new GenreHibernate();
    private Committee committee = new Committee();

    @Test
    @Order(1)
    public void testAdd(){
        committee.setGenre(genreHibernate.getById(1));
        Committee retCommittee = committeeHibernate.add(committee);
        committee.setId(retCommittee.getId());
        assertEquals(committee, retCommittee);
    }

    @Test
    @Order(2)
    public void testGetById(){
        assertEquals(committee, committeeHibernate.getById(committee.getId()));
    }

    @Test
    @Order(3)
    public void testGetAll(){
        assertTrue(committeeHibernate.getAll().size() > 0);
    }

    @Test
    @Order(4)
    public void testUpdate(){
        committee.setGenre(genreHibernate.getById(2));
        committeeHibernate.update(committee);
        assertEquals(committee, committeeHibernate.getById(committee.getId()));
    }

    @Test
    @Order(5)
    public void testDelete(){
        committeeHibernate.delete(committee);
        assertTrue(committeeHibernate.getById(committee.getId()) == null);
    }
}
