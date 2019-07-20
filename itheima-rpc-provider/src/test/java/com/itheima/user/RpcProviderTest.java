package com.itheima.user;

import com.itheima.rpc.RpcFramework;

public class RpcProviderTest {

    public static void main(String[] args) {

        // 程序启动以后，需要通过RPC框架启动一个ServerSocket，来接收客户端的请求
        // 请求接收到了以后，就需要通过反射调用目标方法 , method.invoke方法需要一个目标对象，因此我们需要创建目标对象
        UserService userService = new UserServiceImpl() ;
        RpcFramework.export(userService , 1234);


    }

}
