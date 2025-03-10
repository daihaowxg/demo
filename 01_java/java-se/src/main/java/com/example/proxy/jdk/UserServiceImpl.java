package com.example.proxy.jdk;

public class UserServiceImpl implements UserService {
    @Override
    public void login(String username) {
        System.out.println(username + " 登录成功！");
    }
}