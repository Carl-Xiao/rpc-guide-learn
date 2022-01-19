package com.mini.provider.facade;

import com.mini.rpc.common.annotion.RpcService;
import com.mini.rpc.provider.facade.HelloFacade;

/**
 * @author carl-xiao
 * @description facade
 **/
@RpcService(serviceInterface = HelloFacade.class, serviceVersion = "1.0.0")
public class HelloFacadeImpl implements HelloFacade {
    @Override
    public String hello(String name) {
        return "provider: " + name;
    }
}
