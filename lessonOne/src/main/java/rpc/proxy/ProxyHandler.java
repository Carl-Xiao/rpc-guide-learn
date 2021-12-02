package rpc.proxy;

import rpc.proxy.impl.HelloServiceImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyHandler implements InvocationHandler {

    Object object;

    public ProxyHandler(Object object) {
        this.object = object;
    }

    /**
     * 创建新的实例
     */
    @SuppressWarnings("unchecked")
    public <T> T newInstance(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, ProxyHandler.this);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("调用之前处理");
        System.out.println("method:" + method);
        Object returnValue = method.invoke(object, args);
        System.out.println("调用结束");
        return returnValue;
    }

    public static void main(String[] args) {
        HelloService realService = new HelloServiceImpl();
        ProxyHandler proxyHandler = new ProxyHandler(realService);
        HelloService proxyService = proxyHandler.newInstance(HelloService.class);
        String value = proxyService.hello("carl");
        System.out.println(value);
    }
}
