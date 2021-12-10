package com.rpc.netty.client;

import com.rpc.model.RpcRequest;
import com.rpc.model.RpcResponse;
import com.rpc.netty.codec.NettyKryoDecoder;
import com.rpc.netty.codec.NettyKryoEncoder;
import com.rpc.serialize.Serizlize;
import com.rpc.serialize.impl.KyroSeriz;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClient implements RpcClient {
    private String host;
    private int port;
    private Bootstrap b;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        b = new Bootstrap();
        Serizlize kryoSerializer = new KyroSeriz();
        b.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        /*自定义序列化编解码器*/
                        // RpcResponse -> ByteBuf
                        ch.pipeline()
                                // ByteBuf -> RpcRequest
                                .addLast(new NettyKryoEncoder(kryoSerializer, RpcRequest.class))
                                .addLast(new NettyKryoDecoder(kryoSerializer, RpcResponse.class))
                                .addLast(new NettyClientHandler());
                    }
                });
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        try {
            ChannelFuture f = b.connect(host, port).sync();
            Channel futureChannel = f.channel();
            if (futureChannel != null) {
                futureChannel.writeAndFlush(rpcRequest).addListener(future -> {
                    if (future.isSuccess()) {
                        log.info(String.format("client send message: %s", rpcRequest.toString()));
                    } else {
                        log.error("Send failed:", future.cause());
                    }
                });
                futureChannel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                RpcResponse rpcResponse = futureChannel.attr(key).get();
                return rpcResponse.getData();
            }
        } catch (InterruptedException e) {
            log.error("occur exception when connect server:", e);
        }
        return null;
    }
}
