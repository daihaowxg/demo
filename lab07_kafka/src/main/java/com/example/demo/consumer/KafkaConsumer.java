package com.example.demo.consumer;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
public class KafkaConsumer {

    // 指定要监听的 topic
    @KafkaListener(topics = "test")
    public void consumeTopic(String msg) {
        // 参数: 从topic中收到的 value值
        System.out.println("收到的信息: " + msg);
    }

}

