package com.revature.controller;

import com.revature.beans.Author;
import com.revature.beans.Editor;
import com.revature.beans.User;
import com.revature.data.AuthorHibernate;
import com.revature.data.EditorHibernate;
import com.revature.data.UserHibernate;
import com.revature.exception.NonUniqueUsernameException;
import io.javalin.http.Context;

import java.util.Set;

public class UserController {
    private static UserHibernate userHibernate = new UserHibernate();
    private static AuthorHibernate authorHibernate = new AuthorHibernate();
    private static EditorHibernate editorHibernate = new EditorHibernate();

    public static void checkLogin(Context ctx){
        System.out.println("Checking login");
        User user = ctx.sessionAttribute("user");
        System.out.println(user);
        if (user != null) {
            System.out.println("Logged in as " + user.getUsername());
            ctx.json(user);
            ctx.status(200);
        } else {
            System.out.println("Not logged in");
            ctx.status(400);
        }
    }

    public static void login(Context ctx){
        System.out.println("Logging In");
        String username = ctx.queryParam("user");
        String password = ctx.queryParam("pass");

        User user = new User();
        try{
            user = userHibernate.getByUsername(username);
            if (!user.getPassword().equals(password)){
                ctx.status(400);
            }else{
                System.out.println(user.getUsername());
                ctx.status(200);
                ctx.json(user);
                ctx.sessionAttribute("user", user);
                User retUser = ctx.sessionAttribute("user");
                System.out.println(retUser);
            }
        } catch (Exception e) {
            if (e.getMessage().contains("No entity found for query")) {
                ctx.status(404);
            }
            e.printStackTrace();
        }
    }

    public static void registerUser(Context ctx){
        System.out.println("Registering User");
        String username = ctx.queryParam("user");
        String password = ctx.queryParam("pass");
        String firstname = ctx.queryParam("firstname");
        String lastname = ctx.queryParam("lastname");
        try{
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            User retUser = userHibernate.add(user);
            System.out.println("Successfully Created User");
            Author author = new Author();
            author.setUser(retUser);
            author.setFirstName(firstname);
            author.setLastName(lastname);
            author.setPoints(100);
            Author retAuthor = authorHibernate.add(author);
            if (retAuthor == null){
                throw new Exception("Null Author Object Returned.");
            }
            System.out.println("Successfully Created Author");
            ctx.sessionAttribute("user", retUser);
            ctx.status(200);
        }catch (NonUniqueUsernameException e) {
            System.out.println("Username is not available.");
            ctx.status(400);
        } catch (Exception e2){
            System.out.println("Something went wrong.");
            e2.printStackTrace();
            ctx.status(404);
        }
    }

    public static void logout(Context ctx){
        System.out.println("Logging out");
        ctx.req.getSession().invalidate();
        checkLogin(ctx);
        ctx.status(200);
    }

    public static void getAuthorEditor(Context ctx){
        Integer id = Integer.valueOf(ctx.pathParam("id"));
        System.out.println("Get Author/Editor with ID: " + id);
        User user = userHibernate.getById(id);
        Set<Author> authorSet = authorHibernate.getAll();
        Set<Editor> editorSet = editorHibernate.getAll();

        System.out.println(editorSet);
        Author retAuthor = null;
        Editor retEditor = null;
        for (Author author : authorSet){
            if (author.getUser().getId() == id){
                retAuthor = author;
            }
        }
        for (Editor editor : editorSet){
            if (editor.getUser().getId() == id){
                retEditor = editor;
            }
        }
        if (retAuthor == null && retEditor == null){
            System.out.println("No author or editor with that ID.");
            ctx.status(400);
        }else if (retAuthor == null){
            System.out.println("Found an editor with that ID");
            ctx.sessionAttribute("editor", retEditor);
            ctx.status(200);
            ctx.json(retEditor);
        }else if (retEditor == null){
            System.out.println("Found an author with that ID");
            ctx.sessionAttribute("author", retAuthor);
            ctx.status(200);
            ctx.json(retAuthor);
        } else{
            System.out.println("Function Error.");
            ctx.status(404);
        }
    }

}
