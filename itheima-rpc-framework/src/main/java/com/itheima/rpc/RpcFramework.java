package com.itheima.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

public class RpcFramework {

    // 将服务暴露出去
    public static void export(Object obj, int port) {

        try {
            // 创建ServerSocket对象
            ServerSocket serverSocket = new ServerSocket(port);
            for (;;) {

                Socket socket = serverSocket.accept();

                // 获取客户端传递过来的参数
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream()) ;
                String methodName = objectInputStream.readUTF();                        // 获取方法名称
                Class<?>[] paramTyps = (Class<?>[]) objectInputStream.readObject();     // 获取方法参数的Class类型

                // 获取调用方法的实际参数类型
                Object[] params = (Object[]) objectInputStream.readObject();

                // 获取目标方法
                Method declaredMethod = obj.getClass().getDeclaredMethod(methodName, paramTyps);
                Object result = declaredMethod.invoke(obj, params);

                // 把方法执行的结果会写给消费方
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream()) ;
                objectOutputStream.writeObject(result);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 产生代理对象
    public static <T> T refe(Class<?> userServiceClass , final String host, final int port) {

        return (T) Proxy.newProxyInstance(userServiceClass.getClassLoader(), new Class[]{userServiceClass}, new InvocationHandler() {

            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                /**
                 * 1、创建一个和服务端通信的Socket对象
                 * 2、通过Socket对象发送要调用方法的参数：被调用方法的名称, 被调用方法的参数的Class类型 , 调用方法是所传入的具体的参数
                 * 3、获取响应结果进行返回
                 */
                // 1、创建一个和服务端通信的Socket对象
                Socket socket = new Socket(host, port);
                try {

                    // 2、通过Socket对象发送要调用方法的参数：被调用方法的名称, 被调用方法的参数的Class类型 , 调用方法是所传入的具体的参数
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeUTF(method.getName());
                    objectOutputStream.writeObject(method.getParameterTypes());
                    objectOutputStream.writeObject(args);

                    // 3、获取响应结果进行返回
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    Object result = objectInputStream.readObject();
                    return result;

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    socket.close();
                }

                return null;
            }

        });
    }

}
