package com.revature.data;

import com.revature.beans.Story;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.util.Date;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StoryHibernateTest {
    private StoryHibernate storyHibernate = new StoryHibernate();

    private Story story = new Story();
    private Date date = new Date();
    private Timestamp timestampDate = new Timestamp(date.getTime());

    @Test
    @Order(1)
    public void testAdd(){
        story.setTitle("TITLE");
        story.setCompletionDate(timestampDate);
        story.setTagline("TAGLINE");
        story.setDescription("DESCRIPTION");

        Story retStory = storyHibernate.add(story);
        story.setId(retStory.getId());
        assertEquals(story, retStory);
    }

    @Test
    @Order(2)
    public void testGetById(){
        assertEquals(story, storyHibernate.getById(story.getId()));
    }

    @Test
    @Order(3)
    public void testGetAll(){
        assertTrue(storyHibernate.getAll().size() > 0);
    }

    @Test
    @Order(4)
    public void testUpdate(){
        story.setTitle("UPDATED TITLE");
        storyHibernate.update(story);
        assertEquals(story, storyHibernate.getById(story.getId()));
    }

    @Test
    @Order(5)
    public void testDelete(){
        storyHibernate.delete(story);
        assertTrue(storyHibernate.getById(story.getId()) == null);
    }

}
