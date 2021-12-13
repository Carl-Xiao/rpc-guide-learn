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
    SERVICE_CAN_NOT_BE_FOUND("注册服务未找到"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("服务未实现接口"),
    SERVICE_CAN_NOT_BE_NULL("注册的服务不能为空"),
    REQUEST_NOT_MATCH_RESPONSE("返回结果错误！请求和返回的相应不匹配");
    private final String message;
}
