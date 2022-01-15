package com.mini.provider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author carl-xiao
 * @description rpc注册参数
 **/
@Data
@ConfigurationProperties(prefix = "rpc")
public class RpcProperties {
    /**
     * 服务端口
     */
    private int servicePort;
    /**
     * 注册地址
     */
    private String registryAddr;
    /**
     * 注册协议
     */
    private String registryType;
}
