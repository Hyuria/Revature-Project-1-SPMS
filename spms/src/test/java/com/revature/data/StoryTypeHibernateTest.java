package com.revature.data;

import com.revature.beans.StoryType;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StoryTypeHibernateTest {
    private StoryTypeHibernate storyTypeHibernate = new StoryTypeHibernate();

    private StoryType storyType = new StoryType();

    @Test
    @Order(1)
    public void testAdd(){
        storyType.setType("Essay");
        storyType.setPointValue(5);
        StoryType retStoryType = storyTypeHibernate.add(storyType);
        storyType.setId(retStoryType.getId());
        assertEquals(storyType, retStoryType);
    }

    @Test
    @Order(2)
    public void testGetById(){
        assertEquals(storyType, storyTypeHibernate.getById(storyType.getId()));
    }

    @Test
    @Order(3)
    public void testGetAll(){
        assertTrue(storyTypeHibernate.getAll().size() > 0);
    }

    @Test
    @Order(4)
    public void testUpdate(){
        storyType.setPointValue(3);
        storyTypeHibernate.update(storyType);
        assertEquals(storyType, storyTypeHibernate.getById(storyType.getId()));
    }

    @Test
    @Order(5)
    public void testDelete(){
        storyTypeHibernate.delete(storyType);
        assertTrue(storyTypeHibernate.getById(storyType.getId()) == null);
    }
}
