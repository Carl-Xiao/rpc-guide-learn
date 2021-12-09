package com.rpc.registry.impl;

import com.rpc.exception.RpcErrorMessageEnum;
import com.rpc.exception.RpcException;
import com.rpc.registry.ServiceRegistry;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 注册节点信息
 * @author：carl
 * @date: 2021/12/8
 */
public class DefaultServiceRegistry implements ServiceRegistry {
    /**
     * 并发类
     */
    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    /**
     * 使用同步锁
     *
     * @param service 注册service
     * @param <T>
     */
    @Override
    public synchronized <T> void register(T service) {
        //包含路径的完整类名
        String serviceName = service.getClass().getCanonicalName();
        if (registeredService.contains(serviceName)) {
            return;
        }
        registeredService.add(serviceName);
        //接口名
        Class[] interfaces = service.getClass().getInterfaces();
        if (interfaces.length == 0) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for (Class i : interfaces) {
            serviceMap.put(i.getCanonicalName(), service);
        }
    }

    @Override
    public synchronized Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (null == service) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }
}
