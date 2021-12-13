package com.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * response状态码
 */
@Getter
@AllArgsConstructor
public enum RpcResponseCodeEnum {
    SUCCESS(200, "调用方法成功"),
    FAIL(500, "调用方法失败"),
    NOT_FOUND_METHOD(500, "未找到指定方法"),
    NOT_FOUND_CLASS(500, "未找到指定类");
    private final int code;
    private final String message;
}
