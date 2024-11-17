package com.whdev.tokendemo.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.whdev.tokendemo.mapper.MenuMapper;
import com.whdev.tokendemo.mapper.UserMapper;
import domain.LoginUser;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName, username);

        User user = userMapper.selectOne(wrapper);

        System.out.println(user);
        //如果查询不到数据就通过抛出异常来给出提示
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或者密码错误123");
        }

        List<String> list = menuMapper.selectPermsByUserId(user.getId());
//        List<String> list = new ArrayList<>(Arrays.asList("test", "admin"));

        //如果找到对应用户，封装成UserDetails对象返回
        return new LoginUser(user, list);
    }
}
