package io.github.daihaowxg.demo.mq.pubsub;

import io.github.daihaowxg.demo.mq.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 发布订阅消息
 * @author wxg
 * @since 2025/4/11
 */
@Service
public class TaskPublisher {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void publish(Task task) {
        redisTemplate.convertAndSend(RedisPubSubConfig.CHANNEL_KEY, task);
        System.out.println("发布事件: " + task);
    }
}
