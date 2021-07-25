package com.tju.myproject.utils;

import org.springframework.util.DigestUtils;

import java.util.Map;
import java.util.TreeMap;

public class SignUtil {
    //签名算法
    public static class sign{
        public String sign(Map<String, String> map1, String key){
            Map<String, String> map = new TreeMap<String,String>(map1);
            StringBuilder sb = new StringBuilder();
            for(Map.Entry<String,String> entry : map.entrySet()){
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            sb.append("key=").append(key);
            return DigestUtils.md5DigestAsHex(sb.toString().getBytes()).toUpperCase();
        }
    }
}
