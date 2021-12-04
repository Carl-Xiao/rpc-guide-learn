package com.rpc.remote;

import com.rpc.common.RpcRequest;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @description: 抽奖系统
 * @author：carl
 * @date: 2021/12/4
 */
public class RpcThread implements Runnable {
    Socket socket;
    Object service;

    public RpcThread(Socket socket, Object service) {
        this.socket = socket;
        this.service = service;
    }

    @Override
    public void run() {
        //处理请求服务
        InputStream inputStream = null;
        OutputStream outputStream = null;
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            objectInputStream = new ObjectInputStream(inputStream);
            objectOutputStream = new ObjectOutputStream(outputStream);
            //读取到实体类
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            //接口名
            String methodName = rpcRequest.getMethodName();
            //接口参数
            Method method = service.getClass().getMethod(methodName, rpcRequest.getParamTypes());
            //参数
            Object[] parameters = rpcRequest.getParameters();

            Object result = method.invoke(service, parameters);
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
