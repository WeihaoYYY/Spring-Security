package com.whdev.tokendemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@MapperScan("com.whdev.tokendemo.mapper")
@ComponentScan(basePackages = {"com.whdev.tokendemo", "Config"}) // 确保包含 SecurityConfig 的包
public class TokenDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TokenDemoApplication.class, args);
        System.out.println("TokenDemoApplication started.");
    }

}
