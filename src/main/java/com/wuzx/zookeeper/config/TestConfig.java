package com.wuzx.zookeeper.config;

import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestConfig {

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
    public void getConf() throws Exception {
        WatchCallBack watchCallBack = new WatchCallBack(zk);

        final MyConf myConf = new MyConf();
        watchCallBack.setConf(myConf);
        
        
        watchCallBack.aWait();
        // 1.节点不存在
        
        while (true) {
            System.out.println(myConf.getConf());
            Thread.sleep(1000);
        }
    }
}
