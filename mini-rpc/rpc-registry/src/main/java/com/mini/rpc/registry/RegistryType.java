package com.mini.rpc.registry;

/**
 * @author carl-xiao
 * @description
 **/
public enum RegistryType {
    ZOOKEEPER,
    EUREKA;
    private String type;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
