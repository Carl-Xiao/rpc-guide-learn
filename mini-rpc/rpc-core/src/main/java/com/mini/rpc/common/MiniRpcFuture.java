package com.mini.rpc.common;

import io.netty.util.concurrent.Promise;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author carl-xiao
 * @description
 **/
@Data
@AllArgsConstructor
public class MiniRpcFuture<T> {
    private Promise<T> promise;
    private long timeout;
}
