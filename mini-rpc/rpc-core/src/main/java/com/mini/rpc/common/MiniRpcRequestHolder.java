package com.mini.rpc.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author carl-xiao
 * @description
 **/
public class MiniRpcRequestHolder {
    /**
     * requestId生成器
     */
    public final static AtomicLong REQUEST_ID_GEN = new AtomicLong(0);
    /**
     * request_map map类
     */
    public static final Map<Long, MiniRpcFuture<MiniRpcResponse>> REQUEST_MAP = new ConcurrentHashMap<>();
}
