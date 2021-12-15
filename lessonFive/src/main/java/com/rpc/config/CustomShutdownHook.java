package com.rpc.config;

import com.rpc.utils.thread.ThreadPoolFactoryUtils;
import com.rpc.utils.zk.CuratorHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

/**
 * @description: 抽奖系统
 * @author：carl
 * @date: 2021/12/15
 */
@Slf4j
public class CustomShutdownHook {
    private final ExecutorService threadPool = ThreadPoolFactoryUtils.createDefaultThreadPool("custom-shutdown-hook-rpc-pool");
    private static final CustomShutdownHook CUSTOM_SHUTDOWN_HOOK = new CustomShutdownHook();

    public static CustomShutdownHook getCustomShutdownHook() {
        return CUSTOM_SHUTDOWN_HOOK;
    }

    public void clearAll() {
        log.info("addShutdownHook for clearAll");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            CuratorHelper.clearRegistry();
            threadPool.shutdown();
        }));
    }


}
