package com.rpc.proxy;

import cn.hutool.core.util.IdUtil;
import com.rpc.model.RpcRequest;
import com.rpc.model.RpcResponse;
import com.rpc.netty.client.RpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;

/**
 * @description: 动态代理类。当动态代理对象调用一个方法的时候，实际调用的是下面的 invoke 方法。
 * 正是因为动态代理才让客户端调用的远程方法像是调用本地方法一样（屏蔽了中间过程）
 * @author：carl
 * @date: 2021/12/4
 */
public class RpcClientProxy implements InvocationHandler {
    /**
     * 用于发送请求给服务端，对应netty两种实现方式
     */
    private RpcClient rpcClient;

    public RpcClientProxy(RpcClient rpcClient) {
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
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, RpcClientProxy.this);
    }

    /**
     * 当你使用代理对象调用方法的时候实际会调用到这个方法。代理对象就是你通过上面的 getProxy 方法获取到的对象
     *
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String uuid = IdUtil.fastSimpleUUID();
        RpcRequest rpcRequest = RpcRequest.builder().methodName(method.getName())
                .requestId(uuid)
                .parameters(args)
                .interfaceName(method.getDeclaringClass().getName())
                .paramTypes(method.getParameterTypes())
                .build();
        //shiyong yibu
        CompletableFuture<RpcResponse> completableFuture = rpcClient.sendFutureRpcRequest(rpcRequest);
        Object object = completableFuture.get().getData();
        return object;
    }


}
