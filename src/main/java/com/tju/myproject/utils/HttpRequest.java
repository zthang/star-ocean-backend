package com.tju.myproject.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    public static String sendGet(String url) throws UnsupportedEncodingException, IOException {
        return sendGet(url, null);
    }
    public static String sendGet(String url, Map<String, String> header) throws UnsupportedEncodingException, IOException {
        String result = "";
        BufferedReader in = null;
        String urlNameString = url;
        URL realUrl = new URL(urlNameString);
        // 打开和URL之间的连接
        URLConnection connection = realUrl.openConnection();
        //设置超时时间
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(15000);
        // 设置通用的请求属性
        if (header != null) {
            Iterator<Map.Entry<String, String>> it = header.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                System.out.println(entry.getKey() + ":::" + entry.getValue());
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        Map<String, List<String>> map = connection.getHeaderFields();
        // 遍历所有的响应头字段
        for (String key : map.keySet()) {
            System.out.println(key + "--->" + map.get(key));
        }
        // 定义 BufferedReader输入流来读取URL的响应，设置utf8防止中文乱码
        in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        if (in != null) {
            in.close();
        }
        return result;
    }
    public static HttpEntity<Map<String, Object>> generatePostJson(Map<String, Object> jsonMap) {

        //如果需要其它的请求头信息、都可以在这里追加
        HttpHeaders httpHeaders = new HttpHeaders();

        MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");

        httpHeaders.setContentType(type);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(jsonMap, httpHeaders);

        return httpEntity;
    }
}
