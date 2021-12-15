package com.rpc.netty.client;

import com.rpc.factory.SingletonFactory;
import com.rpc.model.RpcRequest;
import com.rpc.model.RpcResponse;
import com.rpc.registry.ServiceDiscovery;
import com.rpc.registry.ServiceRegistry;
import com.rpc.registry.impl.ZkDiscovery;
import com.rpc.registry.impl.ZkServiceRegistry;
import com.rpc.utils.zk.CuratorHelper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class NetttyClientTransport implements RpcClient {

    private ChannelClientProvider channelClientProvider;
    private final ServiceDiscovery serviceDiscovery;
    private final UnprocessedRequests unprocessedRequests;

    public NetttyClientTransport(ChannelClientProvider channelClientProvider) {
        this.serviceDiscovery = new ZkDiscovery();
        this.channelClientProvider = channelClientProvider;
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        try {
            InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
            Channel futureChannel = channelClientProvider.get(inetSocketAddress);
            if (futureChannel != null) {
                futureChannel.writeAndFlush(rpcRequest).addListener(future -> {
                    if (future.isSuccess()) {
                        log.info(String.format("client send message: %s", rpcRequest.toString()));
                    } else {
                        log.error("Send failed:", future.cause());
                    }
                });
                futureChannel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse" + rpcRequest.getRequestId());
                RpcResponse rpcResponse = futureChannel.attr(key).get();
                return rpcResponse.getData();
            } else {
                log.error("连接失败");
            }
        } catch (InterruptedException e) {
            log.error("occur exception when connect server:", e);
        }
        return null;
    }

    @Override
    public CompletableFuture<RpcResponse> sendFutureRpcRequest(RpcRequest rpcRequest) {
        // 构建返回值
        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
        try {
            InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
            Channel channel = channelClientProvider.get(inetSocketAddress);
            if (channel != null && channel.isActive()) {
                // 放入未处理的请求
                unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
                channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        log.info("client send message: {}", rpcRequest);
                    } else {
                        future.channel().close();
                        resultFuture.completeExceptionally(future.cause());
                        log.error("Send failed:", future.cause());
                    }
                });
            } else {
                throw new IllegalStateException();
            }
        } catch (Exception e) {
            unprocessedRequests.remove(rpcRequest.getRequestId());
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        return resultFuture;
    }


}
