package com.tju.myproject.service;

import com.tju.myproject.entity.ResultEntity;
import com.tju.myproject.entity.WeChatLoginModel;

import java.util.Map;

public interface LoginService {
    ResultEntity weChatLogin(Map data);
    ResultEntity getToken(Map data);
    String getPhoneNumber(Map data);
    ResultEntity messagePush(Map data);
}
