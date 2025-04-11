package com.example.demo.mq.list;

import com.example.demo.mq.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 使用 Redis 的 List 数据结构，通过 LPUSH（或 RPUSH）和 RPOP（或 LPOP）操作实现简单的消息队列。
 * 生产者将消息推入 List，消费者从 List 中取出消息，类似 FIFO（先进先出）队列。
 * @author wxg
 * @since 2025/4/11
 */
@Service
public class ListQueueService {
    private static final String QUEUE_KEY = "task-queue";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 2. 生产者推送消息
    public void produce(Task task) {
        redisTemplate.opsForList().leftPush(QUEUE_KEY, task);
    }

    // 4. 消费者获取消息
    public Task consume() {
        return (Task) redisTemplate.opsForList().rightPop(QUEUE_KEY);
    }

    /**
     * 阻塞等待 10 秒，如果 10 秒内没有消息，则返回 null
     * @return
     */
    public Task consumeBlocked() {
        return (Task) redisTemplate.opsForList().rightPop(QUEUE_KEY, 10, TimeUnit.SECONDS);
    }
}
