package com.tju.myproject.controller;

import com.tju.myproject.entity.ResultEntity;
import com.tju.myproject.entity.User;
import com.tju.myproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController
{
    @Autowired
    private UserService userService;
    @GetMapping("/getUserInfo")
    public ResultEntity getUserInfo(Integer userID)
    {
        User u=userService.getUserByUserID(userID);
        return new ResultEntity(200,null,u);
    }
}
