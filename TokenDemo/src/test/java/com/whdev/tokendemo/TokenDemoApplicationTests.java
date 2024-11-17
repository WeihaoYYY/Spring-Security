package com.whdev.tokendemo;

import com.whdev.tokendemo.mapper.MenuMapper;
import com.whdev.tokendemo.mapper.UserMapper;
import domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootTest
class TokenDemoApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Test
    void contextLoads() {
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
        System.out.println(users);
    }

    @Test
    public void testPasswordEncoder() {
        // 创建 PasswordEncoder 实例
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // 原始密码
        String rawPassword = "bb"; // 替换为你想加密的密码

        // 加密后的密码
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // 输出加密结果
        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);
    }


    @Test
    public void testSelectPermsByUserId(){
        //L表示Long类型
        List<String> list = menuMapper.selectPermsByUserId(2L);
        System.out.println(list);
    }

}
