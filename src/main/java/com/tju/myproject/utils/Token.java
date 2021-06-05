package com.tju.myproject.utils;


import com.alibaba.fastjson.JSONObject;
import com.tju.myproject.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class Token {

    private String secret;
    @Value("${jwt.secret}")
    public void setSecret(String secret_) {
        secret = secret_;
    }

    private long expire;
    @Value("${jwt.expire}")
    public void setExpire(Long expire_) {
        expire = expire_;
    }

    private String header;
    @Value("${jwt.header}")
    public void setHeader(String header_) {
        header = header_;
    }
    /*
     * 根据身份ID标识，生成Token
     */
    public String getToken (User user, String session_key){
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);
        Map<String,Object> maps = new HashMap<>();
        maps.put("user",user);
        maps.put("sessionKey",session_key);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(JSONObject.toJSONString(maps))
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    /*
     * 获取 Token 中注册信息
     */
    public Claims getTokenClaim (String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}