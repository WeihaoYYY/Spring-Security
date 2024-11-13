package com.whdev.tokendemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
