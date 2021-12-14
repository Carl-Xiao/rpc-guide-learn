package com.rpc.netty.server;

import com.rpc.model.RpcRequest;
import com.rpc.model.RpcResponse;
import com.rpc.netty.codec.NettyKryoServerDecoder;
import com.rpc.netty.codec.NettyKryoServerEncoder;
import com.rpc.provider.ServiceProvider;
import com.rpc.provider.impl.ServiceProviderImpl;
import com.rpc.registry.ServiceRegistry;
import com.rpc.registry.impl.ZkServiceRegistry;
import com.rpc.serialize.impl.KyroSeriz;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class NettyServer {
    /**
     * 启动端口号
     */
    private int port;
    /**
     * 地址
     */
    private String host;
    /**
     * 注册中心
     */
    private ServiceRegistry serviceRegistry;
    private ServiceProvider serviceProvider;
    /**
     * 序列化协议
     */
    private KyroSeriz kyroSeriz;

    public NettyServer(String host, int port) {
        this.host = host;
        this.port = port;
        kyroSeriz = new KyroSeriz();
        serviceRegistry = new ZkServiceRegistry();
        serviceProvider = new ServiceProviderImpl();
    }

    /**
     * 发布服务
     * @param service
     * @param serviceClass
     * @param <T>
     */
    public <T> void publishService(Object service, Class<T> serviceClass) {
        serviceProvider.addServiceProvider(service);
        serviceRegistry.register(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
        start();
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new NettyKryoServerDecoder(kyroSeriz, RpcRequest.class))
                                    .addLast(new NettyKryoServerEncoder(kyroSeriz, RpcResponse.class))
                                    .addLast(new NettyServerHandler());
                        }
                    })
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_KEEPALIVE, true);
            // 绑定端口，同步等待绑定成功
            ChannelFuture f = b.bind(host, port).sync();
            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("occur exception when start server:", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
