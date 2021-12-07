package com.rpc.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: rpc request
 * @author：carl
 * @date: 2021/12/7
 */
@Data
@Builder
public class RpcRequest implements Serializable {
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;
}
