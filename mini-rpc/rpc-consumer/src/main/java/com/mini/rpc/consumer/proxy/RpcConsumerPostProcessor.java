package com.mini.rpc.consumer.proxy;

import com.mini.rpc.common.annotion.RpcReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @description: 声明 @RpcReference 注解的成员变量构造出 RpcReferenceBean
 * @author：carl
 * @date: 2022/1/15
 */
@Component
@Slf4j
public class RpcConsumerPostProcessor implements ApplicationContextAware, BeanClassLoaderAware, BeanFactoryPostProcessor {
    //RpcReferenceBean 构造函数使用
    private final String initMethod = "init";

    private ApplicationContext context;

    private ClassLoader classLoader;

    private final Map<String, BeanDefinition> rpcRefBeanDefinitions = new LinkedHashMap<>();

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //生成代理bean对象
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            if (null == beanClassName) {
                continue;
            }
            Class<?> clazz = ClassUtils.resolveClassName(beanClassName, this.classLoader);
            ReflectionUtils.doWithFields(clazz, this::parseRpcReference);
        }
        //注册到bean中心
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        this.rpcRefBeanDefinitions.forEach((beanName, beanDefinition) -> {
            if (context.containsBean(beanName)) {
                throw new IllegalArgumentException("spring context already has a bean named " + beanName);
            }
            registry.registerBeanDefinition(beanName, rpcRefBeanDefinitions.get(beanName));
            log.info("registered RpcReferenceBean {} success.", beanName);
        });
    }

    private void parseRpcReference(Field field) {
        RpcReference annotation = AnnotationUtils.getAnnotation(field, RpcReference.class);
        if (annotation != null) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RpcReferenceBean.class);
            builder.setInitMethodName(initMethod);
            builder.addPropertyValue("interfaceClass", field.getType());
            builder.addPropertyValue("serviceVersion", annotation.serviceVersion());
            builder.addPropertyValue("registryType", annotation.registryType());
            builder.addPropertyValue("registryAddr", annotation.registryAddress());
            builder.addPropertyValue("timeout", annotation.timeout());
            BeanDefinition beanDefinition = builder.getBeanDefinition();
            rpcRefBeanDefinitions.put(field.getName(), beanDefinition);
        }

    }

}
