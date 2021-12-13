package com.rpc.netty_test;

import com.rpc.handler.RegistryRpcProxy;
import com.rpc.netty.client.ChannelClientProvider;
import com.rpc.netty.client.NetttyClientTransport;
import com.rpc.netty.client.NettyClient;
import com.rpc.netty.client.RpcClient;
import com.rpc.service.HelloService;

import java.net.InetSocketAddress;

public class TestClient {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        Integer port = 9999;
        InetSocketAddress inetAddress = new InetSocketAddress(host, port);
        //获取bootstrap
        NettyClient nettyClient = new NettyClient(host, port);
        ChannelClientProvider channelClientProvider = new ChannelClientProvider(nettyClient.getBootstrap());
        //rpc
        RpcClient rpcClient = new NetttyClientTransport(inetAddress, channelClientProvider);
        RegistryRpcProxy rpcClientProxy = new RegistryRpcProxy(rpcClient);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello = helloService.hello("this is a ok");
        System.out.println(hello);
    }
}
