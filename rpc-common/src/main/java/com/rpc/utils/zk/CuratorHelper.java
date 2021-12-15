package com.rpc.utils.zk;

import com.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * zk的helper
 *
 * @author carl-xiao
 */
@Slf4j
public class CuratorHelper {
    private static final int SLEEP_MS_BETWEEN_RETRIES = 100;
    private static final int MAX_RETRIES = 3;
    private static final String CONNECT_STRING = "127.0.0.1:2080";
    private static final int CONNECTION_TIMEOUT_MS = 10 * 1000;
    private static final int SESSION_TIMEOUT_MS = 60 * 1000;
    public static final String ZK_REGISTER_ROOT_PATH = "/rpc-common";
    /**
     * zhucejiedian
     */
    private static Set<String> registeredPathSet = ConcurrentHashMap.newKeySet();
    private static final Map<String, List<String>> serviceAddressMap = new ConcurrentHashMap<>();
    private static CuratorFramework zkClient;

    static {
        RetryPolicy retryPolicy = new RetryNTimes(MAX_RETRIES, SLEEP_MS_BETWEEN_RETRIES);
        zkClient = CuratorFrameworkFactory.builder()
                //要连接的服务器(可以是服务器列表)
                .connectString(CONNECT_STRING)
                .retryPolicy(retryPolicy)
                //连接超时时间，10秒
                .connectionTimeoutMs(CONNECTION_TIMEOUT_MS)
                //会话超时时间，60秒
                .sessionTimeoutMs(SESSION_TIMEOUT_MS)
                .build();
        zkClient.start();
    }

    /**
     * 创建临时节点
     * 临时节点驻存在ZooKeeper中，当连接和session断掉时被删除。
     */
    public static void createEphemeralNode(final String path) {
        try {
            zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (Exception e) {
            log.error("occur exception:", e);
        }
    }

    /**
     * 清空注册中心的数据
     */
    public static void clearRegistry() {
        registeredPathSet.stream().parallel().forEach(p -> {
            try {
                zkClient.delete().forPath(p);
            } catch (Exception e) {
                throw new RpcException(e.getMessage(), e.getCause());
            }
        });
        log.info("服务端（Provider）所有注册的服务都被清空:[{}]", registeredPathSet.toString());
    }

    /**
     * create persistentNode
     *
     * @param path
     */
    public static void createPersistentNode(final String path) {
        try {
            if (registeredPathSet.contains(path) || zkClient.checkExists().forPath(path) != null) {
                log.info("节点已经存在，节点为:[{}]", path);
            } else {
                //eg: /my-rpc/github.javaguide.HelloService/127.0.0.1:9999
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                log.info("节点创建成功，节点为:[{}]", path);
            }
            registeredPathSet.add(path);
        } catch (Exception e) {
            throw new RpcException(e.getMessage(), e.getCause());
        }
    }


    /**
     * 获取某个字节下的子节点
     */
    public static List<String> getChildrenNodes(final String serviceName) {
        if (serviceAddressMap.containsKey(serviceName)) {
            return serviceAddressMap.get(serviceName);
        }
        List<String> result = Collections.emptyList();
        String servicePath = CuratorHelper.ZK_REGISTER_ROOT_PATH + "/" + serviceName;
        try {
            result = zkClient.getChildren().forPath(servicePath);
            serviceAddressMap.put(serviceName, result);
            registerWatcher(serviceName);
        } catch (Exception e) {
            log.error("occur exception:", e);
        }
        return result;
    }

    /**
     * 注册监听
     *
     * @param serviceName 服务名称
     */
    private static void registerWatcher(String serviceName) {
        String servicePath = CuratorHelper.ZK_REGISTER_ROOT_PATH + "/" + serviceName;
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, servicePath, true);
        PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework, pathChildrenCacheEvent) -> {
            List<String> serviceAddresses = curatorFramework.getChildren().forPath(servicePath);
            serviceAddressMap.put(serviceName, serviceAddresses);
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        try {
            pathChildrenCache.start();
        } catch (Exception e) {
            log.error("occur exception:", e);
        }
    }

}
