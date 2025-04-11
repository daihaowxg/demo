package com.example.demo.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author wxg
 * @since 2025/4/11
 */
@Component
@EnableScheduling
public class TaskConsumer {

    private static final String QUEUE_KEY = "task-queue";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 使用 @Scheduled 注解模拟消费者轮询 Redis List（每秒检查一次）。
     * rightPop 从队列尾部获取任务（FIFO）。
     * 实际项目中可以用 BRPOP（阻塞操作）替代轮询，但为简单起见这里用定时任务。
     */
    @Scheduled(fixedDelay = 1000) // 每秒轮询一次
    public void processTask() {
        Task task = (Task) redisTemplate.opsForList().rightPop(QUEUE_KEY);
        if (task != null) {
            System.out.println("Received task: " + task);
            // 模拟处理任务
            System.out.println("Processing task " + task.getTaskId() + ": " + task.getDescription());
        }
    }
}
