package com.mini.rpc.registry;

import com.mini.rpc.common.ServiceMeta;

import java.io.IOException;

/**
 * @author carl-xiao
 * @description 注册中心
 **/
public interface RegistryService {
    /**
     * 注册服务
     *
     * @param serviceMeta
     * @throws Exception
     */
    void register(ServiceMeta serviceMeta) throws Exception;

    /**
     * 取消注册
     *
     * @param serviceMeta
     * @throws Exception
     */
    void unregister(ServiceMeta serviceMeta) throws Exception;

    /**
     * 服务发现
     *
     * @param serviceName     服务名字
     * @param invokerHashCode
     * @return
     * @throws Exception
     */
    ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception;

    /**
     * destroy 销毁
     *
     * @throws IOException
     */
    void destroy() throws IOException;
}
