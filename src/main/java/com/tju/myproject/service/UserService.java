package com.tju.myproject.service;

import com.tju.myproject.entity.ResultEntity;
import com.tju.myproject.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface UserService
{
    int addUser(User user);
    User getUserByPhone(String phone);
    HashMap getUserByUserID(Integer userID);
    ResultEntity addUserAuth(Map m);
    ResultEntity getUserAuthInfo(Integer index, Integer size, ArrayList<Map>club);
    ResultEntity passAuth(Map data);
    ResultEntity notPassAuth(Map data);
    ResultEntity getUsersByName(String name);
    ResultEntity addUserInfo(Map data);
    ResultEntity updateUserInfo(Map data);
    ResultEntity makeUserVip(Integer userID);
}
