package com.revature.service;

import com.revature.beans.Author;
import com.revature.data.AuthorHibernate;

import java.util.Set;

public class AuthorServiceImpl implements AuthorService {
    private AuthorHibernate authorHibernate = new AuthorHibernate();

    @Override
    public Author addUser(Author author) {
        return authorHibernate.add(author);
    }

    @Override
    public Author getUserById(Integer id) {
        return authorHibernate.getById(id);
    }

    @Override
    public void updateUser(Author author) {
        authorHibernate.update(author);
    }

    @Override
    public void deleteUser(Author author) {
        authorHibernate.update(author);
    }

    public Set<Author> getAll(){
        return authorHibernate.getAll();
    }
}
