package rpc.proxy.impl;

import rpc.proxy.HelloService;

/**
 * @author xiaobowen
 */
public class HelloServiceImpl implements HelloService {
    public String hello(String name) {
        return name + ":hello this is a cat";
    }
}
