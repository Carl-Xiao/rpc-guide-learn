package com.rpc.handler;

import com.rpc.model.RpcRequest;
import com.rpc.model.RpcResponse;
import com.rpc.model.RpcResponseCode;
import com.rpc.provider.ServiceProvider;
import com.rpc.provider.impl.ServiceProviderImpl;
import com.rpc.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class RpcRequestHandler {

    private static ServiceProvider serviceProvider;

    static {
        serviceProvider = new ServiceProviderImpl();
    }

    public Object handle(RpcRequest rpcRequest) {
        Object result = null;
        try {
            //从注册中心获取service
            Object service = serviceProvider.getServiceProvider(rpcRequest.getInterfaceName());
            result = invokeTargetMethod(rpcRequest, service);
            log.info("service:{} successful invoke method:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("occur exception", e);
        }
        return result;
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
        if (null == method) {
            return RpcResponse.fail(RpcResponseCode.NOT_FOUND_METHOD);
        }
        return method.invoke(service, rpcRequest.getParameters());
    }

}
