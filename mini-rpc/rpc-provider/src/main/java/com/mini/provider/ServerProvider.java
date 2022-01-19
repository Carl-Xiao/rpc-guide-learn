package com.mini.provider;

import com.mini.codec.MiniRpcDecoder;
import com.mini.codec.MiniRpcEncoder;
import com.mini.handler.RpcRequestHandler;
import com.mini.rpc.common.RpcServiceHelper;
import com.mini.rpc.common.ServiceMeta;
import com.mini.rpc.common.annotion.RpcService;
import com.mini.rpc.registry.RegistryService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author carl-xiao
 * @description
 **/
@Slf4j
public class ServerProvider implements InitializingBean, BeanPostProcessor {
    private String serverAddress;
    private Integer serverPort;
    private final RegistryService serviceRegistry;

    private final Map<String, Object> rpcServiceMap = new HashMap<>();

    public ServerProvider(int serverPort, RegistryService serviceRegistry) {
        this.serverPort = serverPort;
        this.serviceRegistry = serviceRegistry;
    }

    /**
     * 启动服务
     */
    public void startServer() throws UnknownHostException {
        //服务地址
        this.serverAddress = InetAddress.getLocalHost().getHostAddress();
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new MiniRpcEncoder())
                                    .addLast(new MiniRpcDecoder())
                                    .addLast(new RpcRequestHandler(rpcServiceMap));
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture channelFuture = bootstrap.bind(this.serverAddress, this.serverPort).sync();
            log.info("server addr {} started on port {}", this.serverAddress, this.serverPort);
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    /**
     * 服务启动
     *
     * @throws Exception 异常
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(() -> {
            try {
                startServer();
            } catch (Exception e) {
                log.error("start rpc server error.", e);
            }
        }).start();
    }

    /**
     * 处理自定义的bean
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
        if (null == rpcService) {
            return bean;
        }
        Class<?> serviceInterface = rpcService.serviceInterface();
        String serviceName = serviceInterface.getName();
        String serviceVersion = rpcService.serviceVersion();
        try {
            ServiceMeta serviceMeta = new ServiceMeta();
            serviceMeta.setServiceAddr(serverAddress);
            serviceMeta.setServicePort(serverPort);
            serviceMeta.setServiceName(serviceName);
            serviceMeta.setServiceVersion(serviceVersion);
            //注册服务
            serviceRegistry.register(serviceMeta);
            //本地缓存map
            rpcServiceMap.put(RpcServiceHelper.buildServiceKey(serviceMeta.getServiceName(), serviceMeta.getServiceVersion()), bean);
        } catch (Exception e) {
            log.error("failed to register service {}#{}", serviceName, serviceVersion, e);
        }
        return bean;
    }
}
