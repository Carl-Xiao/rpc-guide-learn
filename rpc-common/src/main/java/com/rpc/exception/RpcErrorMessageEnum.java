package com.rpc.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @description: 抽奖系统
 * @author：carl
 * @date: 2021/12/7
 */
@AllArgsConstructor
@Getter
@ToString
public enum RpcErrorMessageEnum {
    SERVICE_INVOCATION_FAILURE("服务调用失败"),
    SERVICE_CAN_NOT_BE_NULL("注册的服务不能为空");
    private final String message;
}
