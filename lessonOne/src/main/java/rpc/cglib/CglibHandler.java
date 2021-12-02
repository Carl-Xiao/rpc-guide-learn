package rpc.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import rpc.proxy.HelloService;
import rpc.proxy.impl.HelloServiceImpl;

import java.lang.reflect.Method;

public class CglibHandler implements MethodInterceptor {
    private Object target;

//    public CglibHandler(Object object) {
//        this.target = object;
//    }

    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("Cglib动态代理，监听开始！");
        //方法执行，参数：target 目标对象 arr参数数组
        Object invoke = method.invoke(target, args);
        System.out.println("Cglib动态代理，监听结束！");
        return invoke;
    }


    public Object getCglibProxy(Object target) {
        this.target = target;
        //cglib的代理类实现方式不一样
        Enhancer enhancer = new Enhancer();
        //设置父类,因为Cglib是针对指定的类生成一个子类，所以需要指定父类
        enhancer.setSuperclass(target.getClass());
        // TODO 设置回调 扩展点
        enhancer.setCallback(this);
        //创建并返回代理对象
        Object result = enhancer.create();
        return result;
    }

    public static void main(String[] args) {
        //实例化CglibProxy对象
        CglibHandler cglib = new CglibHandler();
        //获取代理对象
        HelloService user = (HelloService) cglib.getCglibProxy(new HelloServiceImpl());
        String result = user.hello("admin");
        System.out.println(result);
    }

}
