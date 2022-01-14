package com.mini.rpc.registry.impl;

import com.mini.rpc.common.ServiceMeta;
import com.mini.rpc.registry.RegistryService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

/**
 * @author carl-xiao
 * @description zk的注册实现
 **/
public class ZookeeperRegistry implements RegistryService {
    public static final int BASE_SLEEP_TIME_MS = 1000;
    /**
     * 最大重试次数
     */
    public static final int MAX_RETRIES = 3;
    /**
     * zk存放地址
     */
    public static final String ZK_BASE_PATH = "/mini_rpc";
    /**
     * 服务注册中心
     */
    private final ServiceDiscovery<ServiceMeta> serviceDiscovery;

    public ZookeeperRegistry(String registryAddr) {
        CuratorFramework client = CuratorFrameworkFactory.newClient(registryAddr, new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES));
        client.start();

        JsonInstanceSerializer<ServiceMeta> serializer = new JsonInstanceSerializer<>(ServiceMeta.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMeta.class)
                .client(client)
                .serializer(serializer)
                .basePath(ZK_BASE_PATH)
                .build();
        this.serviceDiscovery.start();
    }


    @Override
    public void register(ServiceMeta serviceMeta) throws Exception {


    }

    @Override
    public void unregister(ServiceMeta serviceMeta) throws Exception {


    }

    @Override
    public ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception {
        return null;
    }
}
