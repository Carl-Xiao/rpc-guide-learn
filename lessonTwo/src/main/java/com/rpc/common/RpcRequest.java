package com.rpc.common;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 请求实体类
 * @author：carl
 * @date: 2021/12/4
 */
@Data
@Builder
public class RpcRequest implements Serializable {
    /**
     * 接口名
     */
    private String interfaceName;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 参数
     */
    private Object[] parameters;

    /**
     * 参数类型
     */
    private Class<?>[] paramTypes;
}
