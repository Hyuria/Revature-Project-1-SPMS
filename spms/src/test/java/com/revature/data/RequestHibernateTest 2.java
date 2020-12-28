package com.revature.data;

import com.revature.beans.*;
import com.revature.exception.NonUniqueUsernameException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RequestHibernateTest {
    private RequestHibernate requestHibernate = new RequestHibernate();
    private UserHibernate userHibernate = new UserHibernate();
    private AuthorHibernate authorHibernate = new AuthorHibernate();
    private EditorHibernate editorHibernate = new EditorHibernate();
    private EditorRoleHibernate editorRoleHibernate = new EditorRoleHibernate();
    private StoryHibernate storyHibernate = new StoryHibernate();


    private Request request = new Request();
    private User authorUser = new User();
    private User editorUser = new User();
    private Story story = new Story();
    private Date date = new Date();
    private Timestamp timestampDate = new Timestamp(date.getTime());

    @Test
    @Order(1)
    public void testAddRequest() throws NonUniqueUsernameException {
        authorUser.setUsername("author");
        authorUser.setPassword("password");
        authorUser = userHibernate.add(authorUser);

        editorUser.setPassword("editor");
        editorUser.setPassword("password");
        editorUser = userHibernate.add(editorUser);

        story.setTitle("TITLE");
        story.setCompletionDate(timestampDate);
        story.setTagline("TAGLINE");
        story.setDescription("DESCRIPTION");
        story = storyHibernate.add(story);


        request.setSender(editorUser);
        request.setReceiver(authorUser);
        request.setMessage("TEST MESSAGE");
        request.setStory(story);
        Request retRequest = requestHibernate.add(request);
        assertEquals(request, retRequest);
    }

    @Test
    @Order(2)
    public void testGetById(){
        assertEquals(request, requestHibernate.getById(request.getId()));
    }

    @Test
    @Order(3)
    public void testGetAll(){
        assertTrue(requestHibernate.getAll().size() > 0);
    }

    @Test
    @Order(4)
    public void testUpdate(){
        request.setMessage("TEST UPDATE");
        requestHibernate.update(request);
        assertEquals(request, requestHibernate.getById(request.getId()));
    }

    @Test
    @Order(5)
    public void testDelete(){
        requestHibernate.delete(request);
        userHibernate.delete(editorUser);
        userHibernate.delete(authorUser);
        storyHibernate.delete(story);
        assertTrue( requestHibernate.getById(request.getId()) == null);
    }

}
