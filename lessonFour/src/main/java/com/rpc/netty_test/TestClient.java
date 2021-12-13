package com.rpc.netty_test;

import com.rpc.handler.RegistryRpcProxy;
import com.rpc.netty.client.NettyClient;
import com.rpc.netty.client.RpcClient;
import com.rpc.service.HelloService;

public class TestClient {
    public static void main(String[] args) {
        RpcClient rpcClient = new NettyClient("127.0.0.1", 9999);
        RegistryRpcProxy rpcClientProxy = new RegistryRpcProxy(rpcClient);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello = helloService.hello("this is a ok");
        System.out.println(hello);
    }
}
