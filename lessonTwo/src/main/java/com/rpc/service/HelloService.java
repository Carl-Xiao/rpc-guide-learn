package com.rpc.service;

import com.rpc.model.RpcResponse;

/**
 * @description: 抽奖系统
 * @author：carl
 * @date: 2021/12/4
 */
public interface HelloService {
    RpcResponse<String> hello(String name);
}
