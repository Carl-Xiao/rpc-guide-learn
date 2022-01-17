package com.mini.rpc.common.annotion;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description:
 * @author：carl
 * @date: 2022/1/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Autowired
public @interface RpcReference {
    /**
     * 注册的版本号
     *
     * @return
     */
    String serviceVersion() default "1.0";

    /**
     * 注册的type类型
     *
     * @return
     */
    String registryType() default "ZOOKEEPER";

    /**
     * 注册地址
     *
     * @return
     */
    String registryAddress() default "127.0.0.1:2181";
    /**
     * 超时时间
     *
     * @return
     */
    long timeout() default 5000;
}
