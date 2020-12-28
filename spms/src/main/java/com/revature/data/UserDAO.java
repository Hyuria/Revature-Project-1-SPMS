package com.revature.data;

import com.revature.beans.User;

public interface UserDAO extends GenericDAO<User>{
    public User getByUsername(String username);
}
