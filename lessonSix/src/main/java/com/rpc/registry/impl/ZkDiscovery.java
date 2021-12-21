package com.rpc.registry.impl;

import com.rpc.registry.ServiceDiscovery;
import com.rpc.utils.zk.CuratorHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;

/**
 * @description: 抽奖系统
 * @author：carl
 * @date: 2021/12/14
 */
@Slf4j
public class ZkDiscovery implements ServiceDiscovery {
    public ZkDiscovery() {
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        //TODO 负载均衡算法添加
        String serviceAddress = CuratorHelper.getChildrenNodes(serviceName).get(0);
        log.info("成功找到服务地址:[{}]", serviceAddress);

        String address = serviceAddress.split(":")[0];
        String port = serviceAddress.split(":")[1];
        InetSocketAddress socketAddress = new InetSocketAddress(address, Integer.parseInt(port));
        return socketAddress;
    }
}
