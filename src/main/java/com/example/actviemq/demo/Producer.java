package com.example.actviemq.demo;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Producer {
    public static void main(String[] args) {
        // 添加两个中间件，可以实现故障转移
        String brokerUrl = "failover:(tcp://192.168.191.5:61616,tcp://192.168.191.8:61616)?initialReconnectDelay=100";
        new ProducerThread(brokerUrl, "queue1").start();
    }

    static class ProducerThread extends Thread {
        String brokerUrl;
        String destinationUrl;

        public ProducerThread(String brokerUrl, String destinationUrl) {
            this.brokerUrl = brokerUrl;
            this.destinationUrl = destinationUrl;
        }

        @Override
        public void run() {
            ActiveMQConnectionFactory connectionFactory;
            Connection conn;
            Session session;

            try {
                // 1、创建连接工厂
                connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
                // 2、创建连接对象md
                conn = connectionFactory.createConnection();
                conn.start();
                // 3、创建会话
                session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
                // 4、创建点对点发送的目标
                Destination destination = session.createQueue(destinationUrl);
                // 5、创建生产者消息
                MessageProducer producer = session.createProducer(destination);
                // 设置生产者的模式，有两种可选 持久化 / 不持久化
                producer.setDeliveryMode(DeliveryMode.PERSISTENT);
                // 6、创建一条文本消息
                String text = "Hello world!";
                TextMessage message = session.createTextMessage(text);
                for (int i = 0; i < 1000; i++) {
                    // 7、发送消息
                    producer.send(message);
                }
                // 8、 关闭连接
                session.close();
                conn.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
