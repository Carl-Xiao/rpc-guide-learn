package com.rpc.registry.impl;

import com.rpc.registry.ServiceDiscovery;
import com.rpc.utils.zk.CuratorHelper;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;

/**
 * @description: 抽奖系统
 * @author：carl
 * @date: 2021/12/14
 */
public class ZkDiscovery implements ServiceDiscovery {

    private final CuratorFramework zkClient;

    public ZkDiscovery() {
        zkClient = CuratorHelper.getZkClient();
        zkClient.start();
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        //TODO 负载均衡算法添加
        String serviceAddress = CuratorHelper.getChildrenNodes(zkClient, serviceName).get(0);
        String address = serviceAddress.split(":")[0];
        String port = serviceAddress.split(":")[1];
        InetSocketAddress socketAddress = new InetSocketAddress(address, Integer.parseInt(port));
        return socketAddress;
    }
}
