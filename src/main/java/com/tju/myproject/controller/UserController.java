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
    @GetMapping("/api/getUserInfoByPhone")
    public ResultEntity getUserInfoByPhone(String phone)
    {
        User u=userService.getUserByPhone(phone);
        return new ResultEntity(200,null,u);
    }
    @PostMapping("/login")
    public ResultEntity weChatLogin(@RequestBody Map data)
    {
        //调用service.weChatLogin(model):后台检查openid是否存在，返回openid对应的用户

        return loginService.weChatLogin(data);
    }
    @PostMapping("/loginByPhone")
    public ResultEntity loginByPhone(@RequestBody Map data)
    {
        //String phoneNumber=loginService.getPhoneNumber(data);
        return loginService.getToken(data);
    }
    @GetMapping("/getDelta")
    public String getDelta()
    {
        return "{\"ops\":[{\"attributes\":{\"bold\":\"strong\"},\"insert\":\"asdasd\"},{\"insert\":\"\\n\"},{\"attributes\":{\"italic\":\"em\"},\"insert\":\"阿斯顿\"},{\"insert\":\"\\n\"},{\"attributes\":{\"alt\":\"img\",\"height\":\"80%\",\"width\":\"120\",\"data-custom\":\"id=0\"},\"insert\":{\"image\":\"http://tmp/t4MBRsOO22bE0d783e2795b4aef0cd60bed69ea54d0b.png\"}},{\"insert\":\"\\n\"},{\"attributes\":{\"alt\":\"img\",\"height\":\"80%\",\"width\":\"80%\",\"data-custom\":\"id=1\"},\"insert\":{\"image\":\"http://tmp/KShguYYcpZ8k41b46ea86b4a62c07315e162ed1caf23.jpg\"}},{\"insert\":\"\\n\\n\\n\"}]}";
    }
}
