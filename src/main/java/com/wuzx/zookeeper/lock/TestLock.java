package com.wuzx.zookeeper.lock;

import com.wuzx.zookeeper.config.ZKUtils;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestLock {

    private ZooKeeper zk;

    @Before
    public void conn() {
        zk = ZKUtils.getZK();
    }

    @After
    public void close() {
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void lock() {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                final WatchCallBack watchCallBack = new WatchCallBack(zk);
                watchCallBack.setThreadName(Thread.currentThread().getName());
                
                // 每一个线程抢琐
                watchCallBack.tryLock();
                
                // 干活
                System.out.println(Thread.currentThread().getName() + "work.....");

                // 释放锁
                watchCallBack.unLock();
                
            }).start();
            
        }

        while (true) {
            
        }
    }
}
