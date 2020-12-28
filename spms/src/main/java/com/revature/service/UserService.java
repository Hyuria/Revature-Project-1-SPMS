package com.revature.service;

import com.revature.beans.User;
import com.revature.exception.NonUniqueUsernameException;

public interface UserService {
    //  CRUD Operations

    //  CREATE
    public User addUser(User user) throws NonUniqueUsernameException;
    //  READ
    public User getUserById(Integer id);
    public User getUserByUsername(String username);
    //  UPDATE
    public void updateUser(User user);
    //  DELETE
    public void deleteUser(User user);
}
