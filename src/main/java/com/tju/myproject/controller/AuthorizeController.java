package com.tju.myproject.controller;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.tju.myproject.entity.ResultEntity;
import com.tju.myproject.entity.User;
import com.tju.myproject.service.UserService;
import com.tju.myproject.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.DigestUtils;

@RestController
public class AuthorizeController
{

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Object login(String username,String password)
    {
        if(username==null||username.length()==0)
            return new ResultEntity(-1,"用户名不能为空!",null);
        if(password==null||password.length()==0)
            return new ResultEntity(-1,"密码不能为空!",null);
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("loginName", username);
        User temp=userService.getUserByUsername(username);
        if(temp!=null)
        {
            if (temp.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes())))
            {
                JSONObject data = jwtHelper.generateToken(claims);
                data.put("userID",temp.getUserID());
                data.put("role",temp.getRole());
                return new ResultEntity(200,"登录成功!",data);
            }
            else
            {
                return new ResultEntity(-1,"密码错误!",null);
            }
        }
        else
        {
            return new ResultEntity(-1,"用户名不存在!",null);
        }
    }
    @PostMapping("/register")
    public Object register(String username,String password)
    {
        if(username==null||username.length()==0)
            return new ResultEntity(-1,"用户名不能为空!",null);
        if(password==null||password.length()==0)
            return new ResultEntity(-1,"密码不能为空!",null);
        if(userService.getUserByUsername(username)==null)
        {
            password=DigestUtils.md5DigestAsHex(password.getBytes());
            User user=new User();
            user.setUsername(username);
            user.setPassword(password);
            if(userService.addUser(user)==1)
                return new ResultEntity(200,"注册成功!",null);
            else
                return new ResultEntity(-1,"数据库错误,注册失败!",null);
        }
        else
        {
            return new ResultEntity(-1,"该用户名已经存在!",null);
        }
    }
}