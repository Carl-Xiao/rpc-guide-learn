package com.rpc.test;

import com.rpc.netty.server.NettyServer;
import com.rpc.service.HelloService;
import com.rpc.service.impl.HelloServiceImpl;

/**
 * @description: 抽奖系统
 * @author：carl
 * @date: 2021/12/14
 */
public class NettyServerMain {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        NettyServer nettyServer = new NettyServer("127.0.0.1", 9999);
        nettyServer.publishService(helloService, HelloService.class);
    }

}
