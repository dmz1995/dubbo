package com.itheima.user;

import com.itheima.rpc.RpcFramework;

import java.lang.reflect.Proxy;

public class RpcConsumerTest {

    public static void main(String[] args) {

        // 通过RPC框架产生一个UserService的代理对象，
        UserService userService = RpcFramework.refe(UserService.class , "127.0.0.1" , 1234) ;
        String result = userService.sayHello("RpcConsumerTest...............");
        System.out.println(result);

    }

}
