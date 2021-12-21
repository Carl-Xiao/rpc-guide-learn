package com.rpc.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 单例工厂
 * @author：carl
 * @date: 2021/12/15
 */
public class SingletonFactory {
    private static Map<String, Object> objectMap = new HashMap<>();

    private SingletonFactory() {
    }

    public static <T> T getInstance(Class<T> c) {
        String key = c.toString();
        Object instance = objectMap.get(key);
        synchronized (c) {
            if (instance == null) {
                try {
                    instance = c.newInstance();
                    objectMap.put(key, instance);
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
        return c.cast(instance);
    }
}
