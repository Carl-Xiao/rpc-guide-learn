package com.mini.rpc.common;

import lombok.Data;

/**
 * @author carl-xiao
 * @description service元数据
 **/
@Data
public class ServiceMeta {
    /**
     * 服务注册名
     */
    private String serviceName;
    /**
     * 服务注册版本
     */
    private String serviceVersion;
    /**
     * 服务注册地址
     */
    private String serviceAddr;
    /**
     * 服务注册端口号
     */
    private int servicePort;
}
