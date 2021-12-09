package com.rpc.handler;

import com.rpc.model.RpcRequest;
import com.rpc.netty.RpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @description: 请求代理
 * @author：carl
 * @date: 2021/12/4
 */
public class RegistryRpcProxy implements InvocationHandler {
    private RpcClient rpcClient;

    public RegistryRpcProxy(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    /**
     * 获取代理对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, RegistryRpcProxy.this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = RpcRequest.builder().methodName(method.getName())
                .parameters(args)
                .interfaceName(method.getDeclaringClass().getName())
                .paramTypes(method.getParameterTypes())
                .build();
        Object object = rpcClient.sendRpcRequest(rpcRequest);
        return object;
    }


}
