package com.rpc.netty.server;

import com.rpc.config.CustomShutdownHook;
import com.rpc.model.RpcRequest;
import com.rpc.model.RpcResponse;
import com.rpc.netty.codec.NettyKryoServerDecoder;
import com.rpc.netty.codec.NettyKryoServerEncoder;
import com.rpc.provider.ServiceProvider;
import com.rpc.provider.impl.ServiceProviderImpl;
import com.rpc.registry.ServiceRegistry;
import com.rpc.registry.impl.ZkServiceRegistry;
import com.rpc.serialize.impl.KyroSeriz;
import com.rpc.utils.zk.CuratorHelper;
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
     *
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
                    // TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 是否开启 TCP 底层心跳机制
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //表示系统用于临时存放已完成三次握手的请求的队列的最大长度,如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                    .option(ChannelOption.SO_KEEPALIVE, true);
            // 绑定端口，同步等待绑定成功
            ChannelFuture f = b.bind(host, port).sync();
            //qingli jiedian
            CustomShutdownHook.getCustomShutdownHook().clearAll();
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
