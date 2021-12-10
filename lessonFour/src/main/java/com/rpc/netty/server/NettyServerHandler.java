package com.rpc.netty.server;

import com.rpc.handler.RpcRequestHandler;
import com.rpc.model.RpcRequest;
import com.rpc.model.RpcResponse;
import com.rpc.registry.ServiceRegistry;
import com.rpc.registry.impl.DefaultServiceRegistry;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static final RpcRequestHandler rpcRequestHandler;
    private static ServiceRegistry serviceRegistry;

    static {
        rpcRequestHandler = new RpcRequestHandler();
        serviceRegistry = new DefaultServiceRegistry();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        RpcRequest rpcRequest = (RpcRequest) msg;
        log.info(String.format("server receive msg: %s", rpcRequest));
        String interfaceName = rpcRequest.getInterfaceName();
        Object service = serviceRegistry.getService(interfaceName);
        Object result = rpcRequestHandler.handle(rpcRequest, service);
        log.info(String.format("server get result: %s", result.toString()));
        ctx.writeAndFlush(RpcResponse.success(result));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("server catch exception");
        cause.printStackTrace();
        ctx.close();
    }
}
