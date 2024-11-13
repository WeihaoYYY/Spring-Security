package com.whdev.tokendemo.controllers;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

/*
    ①自定义登录接口
        调用ProviderManager的方法进行认证如果认证通过生成wt
        把用户信息存入redis中
    ②自定义UserDetailsService
        在这个实现列中去查询数据库中的用户信息
*/

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

}
