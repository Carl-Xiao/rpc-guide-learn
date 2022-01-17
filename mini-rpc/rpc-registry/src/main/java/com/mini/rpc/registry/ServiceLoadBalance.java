package com.mini.rpc.registry;

import java.util.List;

/**
 * @author carl-xiao
 * @description 负载均衡策略
 **/
public interface ServiceLoadBalance<T> {
    T select(List<T> servers, int hashcode);

}
