package com.rpc.netty_test;

import com.rpc.netty.server.NettyServer;
import com.rpc.registry.ServiceRegistry;
import com.rpc.registry.impl.DefaultServiceRegistry;
import com.rpc.service.HelloService;
import com.rpc.service.impl.HelloServiceImpl;

public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        //注册服务
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        //启动服务
        NettyServer registryRpcServer = new NettyServer(9999);
        registryRpcServer.run();
    }
}
