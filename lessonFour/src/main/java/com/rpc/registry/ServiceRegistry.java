package com.rpc.registry;

/**
 * @description: 服务注册
 * @author：carl
 * @date: 2021/12/8
 */
public interface ServiceRegistry {
    /**
     * 泛型 service
     *
     * @param service 注册service
     * @param <T>  泛型
     */
    <T> void register(T service);
    /**
     * 获取service
     *
     * @param serviceName 服务name
     * @return
     */
    Object getService(String serviceName);
}
