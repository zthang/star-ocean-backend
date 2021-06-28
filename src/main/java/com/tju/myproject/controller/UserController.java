package com.tju.myproject.controller;

import com.tju.myproject.entity.ResultEntity;
import com.tju.myproject.entity.User;
import com.tju.myproject.entity.WeChatLoginModel;
import com.tju.myproject.service.LoginService;
import com.tju.myproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController
{
    @Autowired
    private UserService userService;
    @Autowired
    private LoginService loginService;
    @GetMapping("/api/getUserInfo")
    public ResultEntity getUserInfo(Integer userID)throws Exception
    {
        User u=userService.getUserByUserID(userID);
        return new ResultEntity(u!=null?200:-1,null,u);
    }
//    @GetMapping("/api/getUserInfoByPhone")
//    public ResultEntity getUserInfoByPhone(String phone)
//    {
//        User u=userService.getUserByPhone(phone);
//        return new ResultEntity(200,null,u);
//    }
    @PostMapping("/login")
    public ResultEntity weChatLogin(@RequestBody Map data)
    {
        //调用service.weChatLogin(model):后台检查openid是否存在，返回openid对应的用户

        return loginService.weChatLogin(data);
    }
    @PostMapping("/loginByInfo")
    public ResultEntity loginByInfo(@RequestBody Map data)
    {
        //String phoneNumber=loginService.getPhoneNumber(data);
        return loginService.getToken(data);
    }
    @PostMapping("api/addUserAuth")
    public ResultEntity addUserAuth(@RequestBody Map data)
    {
        return userService.addUserAuth(data);
    }
    @PostMapping("api/passAuth")
    public ResultEntity updateUserAuth(@RequestBody Map data)
    {
        return userService.passAuth(data);
    }
    @PostMapping("api/notPassAuth")
    public ResultEntity notPassAuth(@RequestBody Map data)
    {
        return userService.notPassAuth(data);
    }
    @PostMapping("api/sendMessage")
    public ResultEntity sendMessage(@RequestBody Map data){
        return loginService.messagePush(data);
    }
    @GetMapping("api/getUserAuthInfo")
    public ResultEntity getUserAuthInfo(Integer index,Integer size)
    {
        return userService.getUserAuthInfo(index, size);
    }
    @GetMapping("api/getUsersByName")
    public ResultEntity getUsersByName(String name)
    {
        return userService.getUsersByName(name);
    }
    @PostMapping("api/addUserInfo")
    public ResultEntity addUserInfo(@RequestBody Map data)
    {
        return userService.addUserInfo(data);
    }
    @PostMapping("api/updateUserInfo")
    public ResultEntity updateUserInfo(@RequestBody Map data)
    {
        return userService.updateUserInfo(data);
    }
}
