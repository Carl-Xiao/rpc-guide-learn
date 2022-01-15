package com.mini.rpc.registry;

import com.mini.rpc.registry.impl.ZookeeperRegistry;

/**
 * @description:
 * @author：carl
 * @date: 2022/1/15
 */
public class RegistryFactory {

    private static volatile RegistryService registryService;

    public static RegistryService getInstance(String registryAddr, RegistryType type) throws Exception {
        if (null == registryService) {
            synchronized (RegistryFactory.class) {
                if (null == registryService) {
                    switch (type) {
                        case NACOS:
                            break;
                        case ZOOKEEPER:
                            registryService = new ZookeeperRegistry(registryAddr);
                            break;
                    }
                }
            }
        }
        return registryService;
    }


}
