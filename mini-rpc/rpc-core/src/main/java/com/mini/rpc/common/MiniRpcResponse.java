package com.mini.rpc.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author cc
 */
@Data
public class MiniRpcResponse implements Serializable {
    /**
     * 消息体
     */
    private Object data;
    /**
     * 异常错误消息
     */
    private String message;
}
