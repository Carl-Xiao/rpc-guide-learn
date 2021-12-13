package com.rpc.service.impl;

import com.rpc.service.HelloService;

/**
 * @description:
 * @author：carl
 * @date: 2021/12/8
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String word) {
        return "xbw";
    }
}
