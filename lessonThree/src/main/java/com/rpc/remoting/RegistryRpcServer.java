package com.rpc.remoting;

import com.rpc.registry.ServiceRegistry;
import com.rpc.registry.impl.DefaultServiceRegistry;
import com.rpc.service.HelloService;
import com.rpc.service.impl.HelloServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @description: rpc-server
 * @author：carl
 * @date: 2021/12/4
 */
@Slf4j
public class RegistryRpcServer {
    private ExecutorService threadPool;
    private final ServiceRegistry serviceRegistry;


    public RegistryRpcServer(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
        int corePoolSize = 10;
        int maxPoolSize = 100;
        long keepAliveTime = 1;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        this.threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MINUTES, workQueue, threadFactory);
    }
    /**
     * 使用注册service
     *
     * @param port
     */
    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            log.debug("server start {}", port);
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                threadPool.execute(new RegistryRpcThread(socket, serviceRegistry));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        //注册服务
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        //启动服务
        RegistryRpcServer registryRpcServer = new RegistryRpcServer(serviceRegistry);
        registryRpcServer.start(9999);
    }

}
