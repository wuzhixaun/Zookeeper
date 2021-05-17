package com.wuzx.zookeeper.lock;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.common.StringUtils;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class WatchCallBack implements Watcher, AsyncCallback.StringCallback , AsyncCallback.Children2Callback , AsyncCallback.StatCallback {
    private ZooKeeper zk;   
    private String threadName;
    private String pathName;

    CountDownLatch countDownLatch = new CountDownLatch(1);
    
    public WatchCallBack(ZooKeeper zk) {
        this.zk = zk;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    /**
     * 抢琐
     */
    public void tryLock() {

        try {
            System.out.println(threadName+"---create....");
            
            zk.create("/lock", threadName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, this, "ctx");
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放锁
     */
    public void unLock() {
        try {
            zk.delete(pathName, -1);
            System.out.println(threadName+"-- over work");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void processResult(int rx, String path, Object ctx, String name) {
        if (!StringUtils.isEmpty(name)) {

            System.out.println(threadName + "crete node " + name);
            pathName = name;
            zk.getChildren("/", false, this, "sdf");
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                System.out.println("delete note ");
                zk.getChildren("/", false, this, "sdf");
                break;
            case NodeDataChanged:
                break;
            case NodeChildrenChanged:
                break;
            case DataWatchRemoved:
                break;
            case ChildWatchRemoved:
                break;
            case PersistentWatchRemoved:
                break;
        }
            
    }

    @Override
    public void processResult(int rc, String path, Object o, List<String> children, Stat stat) {
        System.out.println(threadName + "lock"+"---"+pathName);
        
        // 遍历
        Collections.sort(children);
        int index = children.indexOf(pathName.substring(1));
        
        // 第一个
        if (index == 0) {
            System.out.println(threadName + "-----is first----");
            try {
                zk.setData("/", threadName.getBytes(), -1);
                countDownLatch.countDown(); 
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        } else {
            zk.exists("/" + children.get(index - 1), this, this, "sdf");   
        }
    }

    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
        //
    }
}
