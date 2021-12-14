package com.rpc.netty.client;

import com.rpc.model.RpcRequest;
import com.rpc.model.RpcResponse;
import com.rpc.registry.ServiceRegistry;
import com.rpc.registry.impl.ZkServiceRegistry;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class NetttyClientTransport implements RpcClient {

    private ChannelClientProvider channelClientProvider;
    private ServiceRegistry serviceRegistry;

    public NetttyClientTransport(ChannelClientProvider channelClientProvider) {
        this.serviceRegistry = new ZkServiceRegistry();
        this.channelClientProvider = channelClientProvider;
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        try {
            InetSocketAddress inetSocketAddress = serviceRegistry.getService(rpcRequest.getInterfaceName());
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
}
