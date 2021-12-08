package com.rpc.service;

import com.rpc.model.RpcResponse;

/**
 * @description: hello service
 * @author：carl
 * @date: 2021/12/8
 */
public interface HelloService {
    RpcResponse<String> hello(String word);
}
