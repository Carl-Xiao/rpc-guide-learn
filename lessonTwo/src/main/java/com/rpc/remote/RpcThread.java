package com.rpc.remote;

import com.rpc.model.RpcRequest;
import com.rpc.model.RpcResponse;
import com.rpc.model.RpcResponseCode;

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
//            //接口名
//            String methodName = rpcRequest.getMethodName();
//            //接口参数
//            Method method = service.getClass().getMethod(methodName, rpcRequest.getParamTypes());
//            //参数
//            Object[] parameters = rpcRequest.getParameters();
            //            Object result = method.invoke(service, parameters);
            //读取到实体类
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            Object result = invokeTargetMethod(rpcRequest);
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

    private Object invokeTargetMethod(RpcRequest rpcRequest) throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        Class<?> cls = Class.forName(rpcRequest.getInterfaceName());
        // 判断类是否实现了对应的接口
        if (!cls.isAssignableFrom(service.getClass())) {
            return RpcResponse.fail(RpcResponseCode.NOT_FOUND_CLASS);
        }
        Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
        if (null == method) {
            return RpcResponse.fail(RpcResponseCode.NOT_FOUND_METHOD);
        }
        return method.invoke(service, rpcRequest.getParameters());
    }


}
