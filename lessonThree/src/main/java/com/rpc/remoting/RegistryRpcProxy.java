package com.rpc.remoting;

import com.rpc.model.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @description: 请求代理
 * @author：carl
 * @date: 2021/12/4
 */
public class RegistryRpcProxy implements InvocationHandler {

    private String host;
    private int port;

    public RegistryRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }
    /**
     * 获取代理对象
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
        RegistryRpcClient registryRpcClient = new RegistryRpcClient();
        Object object = registryRpcClient.sendRpcRequest(rpcRequest, host, port);
        return object;
    }


}
