package com.itheima.user;

public class UserServiceImpl implements UserService {

    public String sayHello(String message) {
        System.out.println("UserServiceImpl....sayHello...." + message);
        return "ok";
    }

}
