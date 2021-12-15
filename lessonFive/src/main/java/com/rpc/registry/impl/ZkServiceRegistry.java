package com.rpc.registry.impl;

import com.rpc.registry.ServiceRegistry;
import com.rpc.utils.zk.CuratorHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * 注册中心zk的service
 */
@Slf4j
public class ZkServiceRegistry implements ServiceRegistry {

    @Override
    public void register(String service, InetSocketAddress socketAddress) {
        StringBuilder servicePath = new StringBuilder(CuratorHelper.ZK_REGISTER_ROOT_PATH).append("/").append(service);
        servicePath.append(socketAddress.toString());
        CuratorHelper.createEphemeralNode(servicePath.toString());
        log.info("节点创建成功，节点为:{}", servicePath);
    }
}
