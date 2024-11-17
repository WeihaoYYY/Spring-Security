package com.whdev.tokendemo.service;

import domain.ResponseResult;
import domain.User;

public interface LoginService {

    ResponseResult login(User user);

    ResponseResult logout();

}
