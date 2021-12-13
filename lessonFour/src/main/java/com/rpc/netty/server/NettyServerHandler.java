package com.rpc.netty.server;

import com.rpc.handler.RpcRequestHandler;
import com.rpc.model.RpcRequest;
import com.rpc.model.RpcResponse;
import com.rpc.registry.ServiceRegistry;
import com.rpc.registry.impl.DefaultServiceRegistry;
import com.rpc.utils.ThreadPoolFactoryUtils;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static final RpcRequestHandler rpcRequestHandler;
    private static ServiceRegistry serviceRegistry;
    /**
     * 线程池
     */
    private static ExecutorService threadPool;

    static {
        rpcRequestHandler = new RpcRequestHandler();
        serviceRegistry = new DefaultServiceRegistry();
        threadPool = ThreadPoolFactoryUtils.createDefaultThreadPool("netty-server-handler-rpc-pool");

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        threadPool.execute(() -> {
            try {
                RpcRequest rpcRequest = (RpcRequest) msg;
                log.info(String.format("server receive msg: %s", rpcRequest));
                String interfaceName = rpcRequest.getInterfaceName();
                Object service = serviceRegistry.getService(interfaceName);
                Object result = rpcRequestHandler.handle(rpcRequest, service);
                log.info(String.format("server get result: %s", result.toString()));
                ChannelFuture f = ctx.writeAndFlush(RpcResponse.success(result));
                f.addListener(ChannelFutureListener.CLOSE);
            } finally {
                //TODO 确保 ByteBuf 被释放，不然可能会有内存泄露问题
                //资源问题
                ReferenceCountUtil.release(msg);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("server catch exception");
        cause.printStackTrace();
        ctx.close();
    }
}
