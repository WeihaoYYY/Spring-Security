package com.whdev.tokendemo.handler;

import com.alibaba.fastjson.JSON;
import com.whdev.tokendemo.Utils.WebUtils;
import domain.ResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
//这个类只处理授权异常，不处理认证异常
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    //第一个参数是请求对象，第二个参数是响应对象，第三个参数是异常对象。把异常封装成认证的对象，然后封装到handle方法
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //ResponseResult是我们在domain目录写好的实体类。HttpStatus是spring提供的枚举类，FORBIDDEN表示403状态码
        ResponseResult result = new ResponseResult(HttpStatus.FORBIDDEN.value(), "User Not Authorized - AccessDeniedHandlerImpl");
        //把上面那行拿到的result对象转换为JSON字符串
        String json = JSON.toJSONString(result);
        //WebUtils是我们在utils目录写好的类
        WebUtils.renderString(response,json);

    }
}