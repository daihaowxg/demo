package com.example.demo.mq.pubsub;

import com.example.demo.mq.Task;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.io.ObjectInputStream;

/**
 * 消息订阅者
 * @author wxg
 * @since 2025/4/11
 */
@Component
public class TaskConsumer1 implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        Task task = serializer.deserialize(message.getBody(), Task.class);
        System.out.println("订阅者 1 收到消息：" + task);
    }
}
