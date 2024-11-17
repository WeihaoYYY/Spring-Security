package com.whdev.tokendemo.service.impl;

import com.mysql.cj.log.Log;
import com.whdev.tokendemo.Utils.JwtUtil;
import com.whdev.tokendemo.Utils.RedisCache;
import com.whdev.tokendemo.service.LoginService;
import domain.LoginUser;
import domain.ResponseResult;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private RedisCache redisCache;


    @Override
    public ResponseResult login(User user) {
        //1. AuthenticationManager
        // 这个UserNamePasswordAuthenticationToken是集成了AbstractAuthenticationToken的AuthenticationManager的实现类
        // 用把用户名和密码封装成 UsernamePasswordAuthenticationToken对象
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());

        // 这个manager.authenticate(token)会调用UserDetailsServiceImpl的loadUserByUsername方法
        Authentication authenticate = manager.authenticate(token);

        //1.1 如果认证没有通过，返回错误信息
        if(Objects.isNull(authenticate)){
            //@ControllerAdvice全局捕获的注解是捕获springmvc层的，这里是过滤层，还没到controller层就已经被过滤器拦截了，所以直接抛异常
            throw new RuntimeException("User Not Found - LoginServiceImpl.login");
        }

        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();

        //1.2 如果认证通过，用userId生成jwt，并用ResponseResult返回
        String id = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(id);

        Map<String, String > map = new HashMap<>();
        map.put("token", jwt);

        //2. 把完整的用户信息存入redis中，userId作为key
        redisCache.setCacheObject("login:"+id, loginUser);


        return new ResponseResult(200, "Login In Successful", map);
    }

    @Override
    public ResponseResult logout() {
        //1. 从SecurityContextHolder中获取用户信息
        UsernamePasswordAuthenticationToken token =
                (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        LoginUser user = (LoginUser) token.getPrincipal();

        Long id = user.getUser().getId();

        //2. 删除redis中的用户信息
        redisCache.deleteObject("login:" + id);

        return new ResponseResult(200, "Logout Successful");
    }
}
