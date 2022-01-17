package com.mini.rpc.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author carl-xiao
 * @description
 **/
@Data
public class MiniRpcRequest implements Serializable {
    /**
     * service版本号
     */
    private String serviceVersion;
    /**
     * 实现类
     */
    private String className;
    /**
     * 方法
     */
    private String methodName;
    /**
     * 参数类型
     */
    private Object[] params;
    /**
     * 参数
     */
    private Class<?>[] parameterTypes;
}
