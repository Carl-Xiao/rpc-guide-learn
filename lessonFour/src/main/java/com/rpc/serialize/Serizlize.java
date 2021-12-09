package com.rpc.serialize;

/**
 * 序列化接口
 *
 * @author carl-xiao
 */
public interface Serizlize {
    /**
     * 序列号接口
     *
     * @param obj
     * @return
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     *
     * @param bytes 序列化后的字节数组
     * @param clazz 类
     * @param <T>
     * @return 反序列化的对象
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);

}
