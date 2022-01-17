package com.mini.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @author carl-xiao
 * @description 消息协议
 **/
@Data
public class MiniRpcProtocol<T> implements Serializable {
    private MsgHeader header;
    private T body;
}
