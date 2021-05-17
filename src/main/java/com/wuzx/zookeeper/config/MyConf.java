package com.wuzx.zookeeper.config;

public class MyConf {
    private String conf;

    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }

    @Override
    public String toString() {
        return "MyConf{" +
                "conf='" + conf + '\'' +
                '}';
    }
}
