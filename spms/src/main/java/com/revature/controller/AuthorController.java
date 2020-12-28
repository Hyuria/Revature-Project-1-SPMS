package com.revature.controller;

import com.revature.beans.Author;
import com.revature.beans.Committee;
import com.revature.beans.Request;
import com.revature.beans.Story;
import com.revature.data.*;
import io.javalin.http.Context;
import org.hibernate.internal.build.AllowSysOut;

import java.util.HashSet;
import java.util.Set;

public class AuthorController {
    private static UserHibernate userHibernate = new UserHibernate();
    private static AuthorHibernate authorHibernate = new AuthorHibernate();
    private static EditorHibernate editorHibernate = new EditorHibernate();
    private static StoryHibernate storyHibernate = new StoryHibernate();
    private static RequestHibernate requestHibernate = new RequestHibernate();
    private static StatusHibernate statusHibernate = new StatusHibernate();
    private static CommitteeHibernate committeeHibernate = new CommitteeHibernate();

    public static void getStories(Context ctx){
        System.out.println("In getStories()");
        Set<Story> storySet = storyHibernate.getAll();
        Set<Story> filteredSet = new HashSet<>();
        Author author = ctx.sessionAttribute("author");
        System.out.println("Author of ID#" + author.getId());
        if (author == null){
            System.out.println("Author object is null.");
            ctx.status(400);
        }else{
            for (Story s : storySet){
                if (s.getAuthor().getId() == author.getId()){
                    filteredSet.add(s);
                }
            }
            System.out.println("Fetched Author Stories \n" + filteredSet);
            ctx.status(200);
            ctx.json(filteredSet);
        }
    }



    public static void getMessages(Context ctx){
        System.out.println("In getMessages()");
        Set<Request> requestSet = requestHibernate.getAll();
        Set<Request> filteredSet = new HashSet<>();
        Author author = ctx.sessionAttribute("author");
        if (author == null){
            System.out.println("Author object is null.");
            ctx.status(400);
        }else{
            for (Request r : requestSet){
                if (r.getSender().getId() == author.getUser().getId()){
                    filteredSet.add(r);
                }
                if (r.getReceiver().getId() == author.getUser().getId()){
                    filteredSet.add(r);
                }
            }
            ctx.status(200);
            ctx.json(filteredSet);
        }
    }


    public static void submitPitch(Context ctx){
        System.out.println("Submit a pitch.");
        Story story = ctx.bodyAsClass(Story.class);
        System.out.println(story);
        story.setStatus(statusHibernate.getById(1));
        Author author = ctx.sessionAttribute("author");
        story.setAuthor(author);
        Set<Committee> committeeSet = committeeHibernate.getAll();
        for (Committee c : committeeSet){
            if (c.getGenre().getId() == story.getGenre().getId()){
                story.setCommittee(c);
            }
        }
        Story retStory = storyHibernate.add(story);
        author.setPoints(author.getPoints() - story.getType().getPointValue());
        authorHibernate.update(author);
        ctx.status(200);
        System.out.println("Added Story #" + retStory.getId());
    }

    public static void updatePitch(Context ctx){
        System.out.println("Updating a pitch.");
        Story story = ctx.bodyAsClass(Story.class);
        try{
            authorHibernate.update(story.getAuthor());
            storyHibernate.update(story);
            ctx.status(200);
        }catch (Exception e){
            e.printStackTrace();
            ctx.status(400);
        }
    }
}
