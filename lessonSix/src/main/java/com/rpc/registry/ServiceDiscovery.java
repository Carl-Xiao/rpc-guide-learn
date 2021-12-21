package com.rpc.registry;

import java.net.InetSocketAddress;

/**
 * @description: 服务注册
 * @author：carl
 * @date: 2021/12/14
 */
public interface ServiceDiscovery {
    /**
     * 获取service
     *
     * @param serviceName 服务name
     * @return
     */
    InetSocketAddress lookupService(String serviceName);
}
