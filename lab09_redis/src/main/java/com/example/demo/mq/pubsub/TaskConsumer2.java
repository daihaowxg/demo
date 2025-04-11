package com.example.demo.mq.pubsub;

import com.example.demo.mq.Task;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * 订阅消息
 * @author wxg
 * @since 2025/4/11
 */
@Component
public class TaskConsumer2 implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        Task task = serializer.deserialize(message.getBody(), Task.class);
        System.out.println("订阅者 2 收到消息：" + task);
    }
}
