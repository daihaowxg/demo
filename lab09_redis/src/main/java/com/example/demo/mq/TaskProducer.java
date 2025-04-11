package com.example.demo.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author wxg
 * @since 2025/4/11
 */
@Service
public class TaskProducer {

    private static final String QUEUE_KEY = "task-queue";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void sendTask(Task task) {
        redisTemplate.opsForList().leftPush(QUEUE_KEY, task);
        System.out.println("Sent task: " + task);
    }
}
