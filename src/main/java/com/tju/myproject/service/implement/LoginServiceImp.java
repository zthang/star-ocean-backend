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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.tju.myproject.utils.HttpRequest.generatePostJson;

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
    @Autowired
    private RestTemplate restTemplate;

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
    public ResultEntity messagePush(Map data)
    {
        Map result=new HashMap();
        try {

            String urlFormat = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
            String url = String.format(urlFormat, appId, appSecret);
            String json = HttpRequest.sendGet(url);

            //将json字符串转化成对象
            result = JSON.parseObject(json);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        Integer state=200;
        String errmsg="access_token获取失败";
        if(result.containsKey("errcode")||result.get("access_token")==null)
            state=-1;
        else {
            try {
                for(String openid:(ArrayList<String>)data.get("openid"))
                {
                    String urlFormat2 = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=%s";
                    String url2 = String.format(urlFormat2, result.get("access_token"));

                    String t=JSON.toJSONString(data.get("message"));
                    Map<String, Object> jsonMap = new HashMap<>();
                    jsonMap.put("touser", openid);
                    jsonMap.put("template_id", data.get("template_id").toString());
                    jsonMap.put("page", data.get("page").toString());
                    jsonMap.put("miniprogram_state", data.get("miniprogram_state").toString());
                    jsonMap.put("lang", "zh_CN");
                    jsonMap.put("data", (data.get("message")));

                    ResponseEntity apiResponse = restTemplate.postForEntity
                            (
                                    url2,
                                    generatePostJson(jsonMap),
                                    String.class
                            );
                    JSONObject o=JSON.parseObject(apiResponse.getBody().toString());
                    if((Integer) o.get("errcode")!=0)
                    {
                        state=-1;
                        errmsg=o.get("errmsg").toString();
                    }
                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return new ResultEntity(state,errmsg,null);
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
            System.out.println(result.get("openid"));
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
        Map u=userDao.getUserByInfo(userData);
        ResultEntity resultEntity;
        if(u!=null)
        {
            JSONObject data = jwtHelper.generateToken(userData);
            data.put("userID",u.get("id"));
            data.put("role",u.get("role"));
            data.put("userInfo",u);
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
