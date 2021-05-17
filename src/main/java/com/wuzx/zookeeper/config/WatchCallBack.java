package com.wuzx.zookeeper.config;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class WatchCallBack implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback {
    
    private ZooKeeper zk;
    private MyConf conf;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public void setConf(MyConf conf) {
        this.conf = conf;
    }

    public WatchCallBack(ZooKeeper zk) {
        this.zk = zk;
    }

    public void aWait() {
        zk.exists("/AppConf", this, this, "ABC");

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
        if (bytes != null) {
            conf.setConf(new String(bytes));
            countDownLatch.countDown();
        }
    }

    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
        // stat 节点的状态
        if (null != stat) {
            zk.getData("/AppConf", this, this, "sdfs");
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("session: " + watchedEvent);

        // 节点数据变更
        switch (watchedEvent.getType()) {
            case None:
                break;
            case NodeCreated:
                // 节点创建那么就要取数据
                zk.getData("/AppConf", this, this, "sdfs");
                break;
            case NodeDeleted:
                // 容忍性
                break;
            case NodeDataChanged:
                System.out.println("NodeDataChanged");
                zk.getData("/AppConf", this, this, "sdfs");
                break;
            case NodeChildrenChanged:
                System.out.println("NodeChildrenChanged");
                break;
            case DataWatchRemoved:
                break;
            case ChildWatchRemoved:
                break;
            case PersistentWatchRemoved:
                break;
        }
    }
}
