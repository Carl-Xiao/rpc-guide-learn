package com.mini.rpc.registry.loadbalance.impl;

import com.mini.rpc.common.ServiceMeta;
import com.mini.rpc.registry.loadbalance.ServiceLoadBalance;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author carl-xiao
 * @description zk的一致性hash算法
 **/
public class ZKConsistentHashLoadBalancer implements ServiceLoadBalance<ServiceInstance<ServiceMeta>> {
    /**
     * 虚节点
     */
    private final static int VIRTUAL_NODE_SIZE = 10;

    /**
     * 虚节点拆分
     */
    private final static String VIRTUAL_NODE_SPLIT = "#";

    @Override
    public ServiceInstance<ServiceMeta> select(List<ServiceInstance<ServiceMeta>> servers, int hashcode) {
        TreeMap<Integer, ServiceInstance<ServiceMeta>> ring = makeConsistentHashRing(servers);
        return allocateNode(ring, hashcode);
    }

    /**
     * 需要构造一个hash环
     */
    private TreeMap<Integer, ServiceInstance<ServiceMeta>> makeConsistentHashRing(List<ServiceInstance<ServiceMeta>> servers) {

        TreeMap<Integer, ServiceInstance<ServiceMeta>> ring = new TreeMap<>();

        for (ServiceInstance<ServiceMeta> instance : servers) {
            for (int i = 0; i < VIRTUAL_NODE_SIZE; i++) {
                ring.put((buildServiceInstanceKey(instance) + VIRTUAL_NODE_SPLIT + i).hashCode(), instance);
            }
        }
        return ring;
    }
    /**
     * 分配节点
     *
     * @param ring
     * @param hashCode
     * @return
     */
    private ServiceInstance<ServiceMeta> allocateNode(TreeMap<Integer, ServiceInstance<ServiceMeta>> ring, int hashCode) {
        // 顺时针找到第一个节点
        Map.Entry<Integer, ServiceInstance<ServiceMeta>> entry = ring.ceilingEntry(hashCode);
        if (entry == null) {
            // 如果没有大于 hashCode 的节点，直接取第一个
            entry = ring.firstEntry();
        }
        return entry.getValue();
    }
    private String buildServiceInstanceKey(ServiceInstance<ServiceMeta> instance) {
        ServiceMeta payload = instance.getPayload();
        return String.join(":", payload.getServiceAddr(), String.valueOf(payload.getServicePort()));
    }


}
