package com.rpc.registry;

import java.net.InetSocketAddress;

/**
 * @description: 服务注册
 * @author：carl
 * @date: 2021/12/8
 */
public interface ServiceRegistry {
    /**
     * 注册服务
     *
     * @param service       服务
     * @param socketAddress 通信信息
     */
    void register(String service, InetSocketAddress socketAddress);

    /**
     * 获取service
     *
     * @param serviceName 服务name
     * @return
     */
    InetSocketAddress getService(String serviceName);
}
