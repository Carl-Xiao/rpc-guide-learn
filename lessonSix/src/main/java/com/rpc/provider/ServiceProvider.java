package com.rpc.provider;

/**
 * @description: 服务提供者
 * @author：carl
 * @date: 2021/12/14
 */
public interface ServiceProvider {
    /**
     * 保存服务提供者
     */
    <T> void addServiceProvider(T service);
    /**
     * 获取服务提供者
     */
    Object getServiceProvider(String serviceName);
}
