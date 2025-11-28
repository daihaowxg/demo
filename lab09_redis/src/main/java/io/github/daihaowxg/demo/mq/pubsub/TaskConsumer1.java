package io.github.daihaowxg.demo.mq.pubsub;

import io.github.daihaowxg.demo.mq.Task;
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
public class TaskConsumer1 implements MessageListener {

    /**
     * 这里用 GenericJackson2JsonRedisSerializer 是因为在序列化的时候用的是 valueSerializer.serialize(value)
     * 而 RedisTemplate<String, Object> 的 valueSerializer 项目里配置的是 GenericToStringSerializer
     *
     * @see com.example.demo.config.RedisConfig
     * @param message message must not be {@literal null}.
     * @param pattern pattern matching the channel (if specified) - can be {@literal null}.
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        Task task = serializer.deserialize(message.getBody(), Task.class);
        System.out.println("订阅者 1 收到消息：" + task);
    }
}
