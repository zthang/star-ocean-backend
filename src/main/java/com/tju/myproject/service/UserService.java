package com.tju.myproject.service;

import com.tju.myproject.entity.User;

public interface UserService
{
    int addUser(User user);
    User getUserByUsername(String username);
    User getUserByUserID(Integer userID);
}
