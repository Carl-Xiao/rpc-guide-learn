package com.mini.serizlization;

import java.io.IOException;

/**
 * @author carl-xiao
 * @description 自定义序列化算法
 **/
public interface RpcSerialization {
    /**
     * 序列化
     *
     * @param obj
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> byte[] serialize(T obj) throws IOException;
    /**
     * 反序列化
     *
     * @param data
     * @param clz
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> T deserialize(byte[] data, Class<T> clz) throws IOException;
}
