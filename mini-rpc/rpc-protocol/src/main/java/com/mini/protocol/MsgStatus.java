package com.mini.protocol;

import lombok.Getter;

/**
 * @author carl-xiao
 * @description 状态类型
 **/
public enum MsgStatus {
    SUCCESS(0),
    FAIL(1);

    @Getter
    private final int code;

    MsgStatus(int code) {
        this.code = code;
    }
}
