package com.mini.rpc.registry;

/**
 * @author carl-xiao
 * @description 选择注册中心的类型
 **/
public enum RegistryType {
    /**
     * zookeeper
     */
    ZOOKEEPER,
    /**
     * nacos
     */
    NACOS;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
