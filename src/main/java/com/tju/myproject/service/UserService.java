package com.tju.myproject.service;

import com.tju.myproject.entity.ResultEntity;
import com.tju.myproject.entity.User;

import java.util.Map;

public interface UserService
{
    int addUser(User user);
    User getUserByPhone(String phone);
    User getUserByUserID(Integer userID);
    ResultEntity addUserAuth(Map m);
    ResultEntity getUserAuthInfo(Integer index, Integer size);
    ResultEntity passAuth(Map data);
    ResultEntity notPassAuth(Map data);
}
