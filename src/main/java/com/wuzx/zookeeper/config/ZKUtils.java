package com.wuzx.zookeeper.config;

import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

public class ZKUtils {

    private static ZooKeeper zk;

    private static String address = "8.135.104.82:2181,120.24.33.237:2181/testLock";
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static DefaultWatch defaultWatch = new DefaultWatch(countDownLatch);

    /**
     * 获取zk 
     * @return
     */
    public static ZooKeeper getZK() {
        try {
            zk = new ZooKeeper(address, 1000, defaultWatch);
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zk; 
    }
}
