package com.example.demo.mq.pubsub;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * 配置发布订阅
 *
 * @author wxg
 * @since 2025/4/11
 */
@Configuration
public class RedisPubSubConfig {

    public static final String CHANNEL_KEY = "task-channel";

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory,
                                                        TaskConsumer1 listener1,
                                                        TaskConsumer2 listener2) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 两个监听器监听同一个channel
        container.addMessageListener(listener1, new ChannelTopic(CHANNEL_KEY));
        container.addMessageListener(listener2, new ChannelTopic(CHANNEL_KEY));
        return container;
    }
}
