package com.tju.myproject.controller;


import com.tju.myproject.entity.ResultEntity;
import com.tju.myproject.utils.RequestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Enumeration;

@ControllerAdvice
public class GlobalExceptionHandler
{
    private static Logger logger = LogManager.getLogger(GlobalExceptionHandler.class.getName());
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResultEntity MethodArgumentTypeMismatchHandler(HttpServletRequest request, Exception e)
    {
        ResultEntity commonMessage = new ResultEntity();
        commonMessage.setMessage("参数匹配错误！");
        commonMessage.setState(-1);
        log(e, request);
        return commonMessage;
    }
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResultEntity NullPointerHandler(HttpServletRequest request, Exception e)
    {
        ResultEntity commonMessage = new ResultEntity();
        commonMessage.setMessage("空指针异常！");
        commonMessage.setState(-1);
        log(e, request);
        return commonMessage;
    }
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    @ResponseBody
    public ResultEntity MaxUploadSizeExceededExceptionHandler(HttpServletRequest request, Exception e)
    {
        ResultEntity commonMessage = new ResultEntity();
        commonMessage.setMessage("文件大小超出限制！");
        commonMessage.setState(-1);
        log(e, request);
        return commonMessage;
    }
    @ExceptionHandler(value = MultipartException.class)
    @ResponseBody
    public ResultEntity MultipartExceptionHandler(HttpServletRequest request, Exception e)
    {
        ResultEntity commonMessage = new ResultEntity();
        commonMessage.setMessage("请检查是否文件过大！");
        commonMessage.setState(-1);
        log(e, request);
        return commonMessage;
    }
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultEntity CommonExceptionHandler(HttpServletRequest request, Exception e)
    {
        ResultEntity commonMessage = new ResultEntity();
        commonMessage.setMessage("异常信息："+e);
        commonMessage.setState(-1);
        log(e, request);
        return commonMessage;
    }
    private void log(Exception ex, HttpServletRequest request)
    {
        logger.error("************************异常开始*******************************");
        logger.error(ex);
        logger.error("请求地址:[{}]",request.getRequestURI());
        try {
            logger.error("请求参数:[{}]", RequestUtils.getRequestJsonString(request));
        }
        catch (IOException e)
        {
            logger.error(e.toString());
        }
        logger.error("异常类型:[{}]", ex.toString());
        StackTraceElement[] error = ex.getStackTrace();
        for (StackTraceElement stackTraceElement : error) {
            logger.error(stackTraceElement.toString());
        }
        logger.error("************************异常结束*******************************");
    }
}
