package com.rpc.netty.client;

import com.rpc.model.RpcRequest;
import com.rpc.model.RpcResponse;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class NetttyClientTransport implements RpcClient {

    private InetSocketAddress inetSocketAddress;
    private ChannelClientProvider channelClientProvider;

    public NetttyClientTransport(InetSocketAddress inetSocketAddress, ChannelClientProvider channelClientProvider) {
        this.inetSocketAddress = inetSocketAddress;
        this.channelClientProvider = channelClientProvider;
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        try {
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
            }
        } catch (InterruptedException e) {
            log.error("occur exception when connect server:", e);
        }
        return null;
    }
}
