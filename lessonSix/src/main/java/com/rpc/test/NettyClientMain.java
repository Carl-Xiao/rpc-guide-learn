package com.rpc.test;

import com.rpc.model.RpcResponse;
import com.rpc.netty.client.ChannelClientProvider;
import com.rpc.netty.client.NetttyClientTransport;
import com.rpc.netty.client.NettyClient;
import com.rpc.netty.client.RpcClient;
import com.rpc.proxy.RpcClientProxy;
import com.rpc.service.HelloService;

import java.net.InetSocketAddress;

/**
 * @description: 抽奖系统
 * @author：carl
 * @date: 2021/12/14
 */
public class NettyClientMain {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        Integer port = 9999;
        //获取bootstrap
        NettyClient nettyClient = new NettyClient(host, port);
        InetSocketAddress socketAddress = new InetSocketAddress(host, port);
        //channel管理
        ChannelClientProvider.put(nettyClient.getBootstrap(), socketAddress);
        //rpc-client proxy
        RpcClient rpcClient = new NetttyClientTransport();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        RpcResponse<String> hello = helloService.helloRpc("this is a ok");
        System.out.println(hello.getData());
    }

}
