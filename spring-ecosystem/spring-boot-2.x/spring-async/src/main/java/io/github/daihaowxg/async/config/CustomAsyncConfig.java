package io.github.daihaowxg.async.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步配置（自定义线程池版本）
 * <p>
 * 演示如何配置自定义线程池来执行异步任务
 * 生产环境建议使用自定义线程池，而不是默认的 SimpleAsyncTaskExecutor
 *
 * @author wxg
 */
@Configuration
@EnableAsync
public class CustomAsyncConfig {

    /**
     * 自定义异步任务执行器
     * <p>
     * 配置线程池参数，控制异步任务的执行
     *
     * @return 线程池执行器
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 核心线程数：线程池维护的最小线程数
        executor.setCorePoolSize(5);

        // 最大线程数：线程池允许创建的最大线程数
        executor.setMaxPoolSize(10);

        // 队列容量：当核心线程都在忙时，新任务会放入队列
        executor.setQueueCapacity(100);

        // 线程名前缀：方便日志追踪
        executor.setThreadNamePrefix("async-event-");

        // 拒绝策略：队列满时的处理策略
        // CallerRunsPolicy: 由调用线程执行任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 线程空闲时间：超过核心线程数的线程在空闲多久后被回收
        executor.setKeepAliveSeconds(60);

        // 等待任务完成后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);

        // 等待时间
        executor.setAwaitTerminationSeconds(60);

        executor.initialize();
        return executor;
    }
}
