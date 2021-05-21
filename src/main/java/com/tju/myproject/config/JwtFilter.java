package com.tju.myproject.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tju.myproject.utils.JwtHelper;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
/**
 * JWT过滤器
 */
public class JwtFilter implements Filter {
    private JwtHelper jwtHelper;
    private List<String> urls = null;
    private static final org.springframework.util.PathMatcher pathMatcher = new AntPathMatcher();
    public JwtFilter(JwtHelper jwtHelper, String[] authorisedUrls) {
        this.jwtHelper = jwtHelper;
        urls = Arrays.asList(authorisedUrls);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        if ("OPTIONS".equals(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpStatus.NO_CONTENT.value()); // HttpStatus.SC_NO_CONTENT = 204
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, Token");
            httpResponse.setHeader("Access-Control-Allow-Methods", "OPTIONS,GET,POST,DELETE,PUT");
        }
        String spath = httpRequest.getServletPath();

        // 验证受保护的接口
        for (String url : urls) {
            if (pathMatcher.match(url, spath)) {
                Map<String, Object> token = jwtHelper.validateTokenAndGetClaims(httpRequest);
                HttpSession session = httpRequest.getSession();
                if (token != null && !token.containsKey("error")) {
                    session.setAttribute("loginName", token.get("loginName"));          //将用户名放在session中
                    chain.doFilter(request, response);
                    return;
                }else if(token == null){
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "token为空");
                    return;
                }else{
                    httpResponse.sendError(Integer.parseInt(token.get("error").toString())==1?20001:20002, Integer.parseInt(token.get("error").toString())==1?"登陆超时，请重新登陆":"token格式错误");
                    return;
                }
            }else{
                chain.doFilter(request, response);
                return;
            }
        }

        chain.doFilter(request, response);
        return;
    }

    @Override
    public void destroy() {

    }
}
