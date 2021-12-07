package com.rpc.remote;

import com.rpc.model.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @description: 请求代理
 * @author：carl
 * @date: 2021/12/4
 */
public class RpcProxy implements InvocationHandler {
    private static final Logger log = LoggerFactory.getLogger(RpcProxy.class);

    private String host;
    private int port;

    public RpcProxy(String host, int port) {
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
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, RpcProxy.this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = RpcRequest.builder().methodName(method.getName())
                .parameters(args)
                .interfaceName(method.getDeclaringClass().getName())
                .paramTypes(method.getParameterTypes())
                .build();
        RpcClient rpcClient = new RpcClient();
        Object object = rpcClient.sendRpcRequest(rpcRequest, host, port);
        return object;
    }


}
