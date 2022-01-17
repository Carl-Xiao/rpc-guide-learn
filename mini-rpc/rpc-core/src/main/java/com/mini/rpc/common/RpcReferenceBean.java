package com.mini.rpc.common;

import lombok.Data;
import org.springframework.beans.factory.FactoryBean;

/**
 * @description: 工厂类实现
 * @author：carl
 * @date: 2022/1/15
 */
@Data
public class RpcReferenceBean implements FactoryBean<Object> {
    private Class<?> interfaceClass;

    private String serviceVersion;

    private String registryType;

    private String registryAddr;

    private long timeout;

    private Object object;

    @Override
    public Object getObject() throws Exception {
        return object;
    }

    @Override
    public Class<?> getObjectType() {

        return interfaceClass;
    }

    // TODO 生成动态代理对象并赋值给 object
    public void init() throws Exception {



    }

}
