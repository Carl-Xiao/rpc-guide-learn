package com.rpc.registry.impl;

import com.rpc.registry.ServiceRegistry;
import com.rpc.utils.zk.CuratorHelper;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * 注册中心zk的service
 */
public class ZkServiceRegistry implements ServiceRegistry {

    private static final Logger logger = LoggerFactory.getLogger(CuratorHelper.class);
    private final CuratorFramework zkClient;

    public ZkServiceRegistry() {
        zkClient = CuratorHelper.getZkClient();
        zkClient.start();
    }

    public void register(String service, InetSocketAddress socketAddress) {
        StringBuilder servicePath = new StringBuilder(CuratorHelper.ZK_REGISTER_ROOT_PATH).append("/").append(service);
        servicePath.append(socketAddress.toString());
        CuratorHelper.createEphemeralNode(zkClient, servicePath.toString());
        logger.info("节点创建成功，节点为:{}", servicePath);
    }

    public InetSocketAddress getService(String serviceName) {
        //TODO 负载均衡算法添加


        String serviceAddress = CuratorHelper.getChildrenNodes(zkClient, serviceName).get(0);
        String address = serviceAddress.split(":")[0];
        String port = serviceAddress.split(":")[1];
        InetSocketAddress socketAddress = new InetSocketAddress(address, Integer.parseInt(port));
        return socketAddress;
    }
}
