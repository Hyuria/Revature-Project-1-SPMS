package com.revature.service;

import com.revature.beans.Author;
import com.revature.beans.User;

public interface AuthorService {
    //  CRUD Operations

    //  CREATE
    public Author addUser(Author author);
    //  READ
    public Author getUserById(Integer id);
    //  UPDATE
    public void updateUser(Author author);
    //  DELETE
    public void deleteUser(Author author);
}
