package com.mini.rpc.common;

/**
 * @description:
 * @author：carl
 * @date: 2022/1/15
 */
public class RpcServiceHelper {
    public static String buildServiceKey(String serviceName, String serviceVersion) {
        return String.join("#", serviceName, serviceVersion);
    }
}
