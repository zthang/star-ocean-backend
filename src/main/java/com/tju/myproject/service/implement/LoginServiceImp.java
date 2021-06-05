package com.tju.myproject.service.implement;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tju.myproject.dao.LoginDao;
import com.tju.myproject.dao.UserDao;
import com.tju.myproject.entity.ResultEntity;
import com.tju.myproject.entity.User;
import com.tju.myproject.entity.WeChatLoginModel;
import com.tju.myproject.service.LoginService;
import com.tju.myproject.utils.HttpRequest;
import com.tju.myproject.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service(value = "loginService")
public class LoginServiceImp implements LoginService
{
    private static String appId;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LoginDao loginDao;
    @Autowired
    private JwtHelper jwtHelper;

    @Value("${wx.appId}")
    public void setAppId(String appIdNew) {
        appId = appIdNew;
    }

    private static String appSecret;

    @Value("${wx.appSecret}")
    public void setAppSecret(String appSecretNew) {
        appSecret = appSecretNew;
    }

    @Override
    public ResultEntity weChatLogin(Map data){
        Map result=new HashMap();
        try {

            // code  -> openid
            String urlFormat = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
            String url = String.format(urlFormat, appId, appSecret, data.get("code"));
            String json = HttpRequest.sendGet(url);

            //将json字符串转化成对象
            result = JSON.parseObject(json);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        Integer state=200;
        if(result.containsKey("errcode")||result.get("openid")==null)
            state=-1;
        else
        {
            Map m=loginDao.getSessionByOpenid((String)result.get("openid"));
            if(m==null||m.isEmpty())
            {
                Integer num=loginDao.insertInfo(result);
                if(num==0)
                    state=-1;
            }
            else if(m.get("session_key")!=result.get("session_key"))
            {
                Integer num=loginDao.updateInfo(result);
                if(num==0)
                    state=-1;
            }
            result.remove("session_key");
        }
        return new ResultEntity(state,"",result);
    }
    public ResultEntity getToken(Map userData)
    {
        String phoneNumber=(String) userData.get("phone");
        User u=userDao.getUserByPhone(phoneNumber);
        ResultEntity resultEntity;
        if(u!=null)
        {
            JSONObject data = jwtHelper.generateToken(userData);
            data.put("userID",u.getId());
            data.put("role",u.getRole());
            resultEntity=new ResultEntity(200,"",data);
        }
        else
            resultEntity=new ResultEntity(-1,"用户不存在",null);
        return resultEntity;
    }
    public String getPhoneNumber(Map data)
    {
        return (String)data.get("phone");
    }
}
