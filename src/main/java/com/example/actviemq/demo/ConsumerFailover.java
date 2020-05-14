package com.example.actviemq.demo;

public class ConsumerFailover {
    public static void main(String[] args) throws InterruptedException {
        // 非failover的公共参数配置通过nested.*，例如 failover:(...)?nested.wireFormat.maxInactivityDuration=1000
        // ?randomize=false 随机选择，默认是顺序
        // 指定优先切换 failover:(tcp://host1:61616,tcp://host2:61616,tcp://host3:61616)?priorityBackup=true&priorityURIs=tcp://local1:61616,tcp://local2:61616
        // maxReconnectDelay重连的最大间隔时间(毫秒)
        String brokerUrl = "failover:(tcp://192.168.191.5:61616,tcp://192.168.191.8:61616)?initialReconnectDelay=100";
        ConsumerThread queue1 = new ConsumerThread(brokerUrl, "queue1");
        queue1.start();
        queue1.join();
    }
}
