package com.rpc.service.impl;

import com.rpc.model.RpcResponse;
import com.rpc.service.HelloService;

import java.util.UUID;

/**
 * @description: 抽奖系统
 * @author：carl
 * @date: 2021/12/8
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String word) {
        return "xbw";
    }

    @Override
    public RpcResponse<String> helloRpc(String word) {
        return RpcResponse.success(word, UUID.randomUUID().toString());
    }
}
