package io.github.daihaowxg.async;

import io.github.daihaowxg.async.service.AsyncService;
import io.github.daihaowxg.async.service.BusinessService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 异步方法测试
 * <p>
 * 演示 @Async 注解在不同场景下的使用
 *
 * @author wxg
 */
@SpringBootTest
@DisplayName("@Async 注解使用示例测试")
class AsyncServiceTest {

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private BusinessService businessService;

    @Test
    @DisplayName("场景 1：无返回值的异步方法")
    void testAsyncMethodWithoutReturn() throws InterruptedException {
        System.out.println("\n========== 场景 1：无返回值的异步方法 ==========");
        
        long startTime = System.currentTimeMillis();
        
        // 调用异步方法
        asyncService.sendEmailAsync("user@example.com", "测试邮件");
        
        long endTime = System.currentTimeMillis();
        System.out.println(">> 主线程耗时: " + (endTime - startTime) + "ms (立即返回)");
        System.out.println(">> 邮件正在后台发送中...");
        
        // 等待异步操作完成（仅用于演示）
        Thread.sleep(3000);
        System.out.println(">> 异步操作已完成");
    }

    @Test
    @DisplayName("场景 2：有返回值的异步方法")
    void testAsyncMethodWithReturn() throws Exception {
        System.out.println("\n========== 场景 2：有返回值的异步方法 ==========");
        
        businessService.getUserDetails(12345L);
    }

    @Test
    @DisplayName("场景 3：批量异步处理")
    void testBatchAsyncProcessing() throws InterruptedException {
        System.out.println("\n========== 场景 3：批量异步处理 ==========");
        
        Long[] orderIds = {1001L, 1002L, 1003L, 1004L, 1005L};
        
        long startTime = System.currentTimeMillis();
        businessService.processBatchOrders(orderIds);
        long endTime = System.currentTimeMillis();
        
        System.out.println(">> 提交批量任务耗时: " + (endTime - startTime) + "ms");
        System.out.println(">> 所有订单正在并发处理中...");
        
        // 等待所有异步操作完成
        Thread.sleep(2000);
        System.out.println(">> 所有订单处理完成");
    }

    @Test
    @DisplayName("场景 4：业务方法中使用异步")
    void testBusinessWithAsync() throws InterruptedException {
        System.out.println("\n========== 场景 4：业务方法中使用异步 ==========");
        
        long startTime = System.currentTimeMillis();
        businessService.registerUser("alice", "alice@example.com");
        long endTime = System.currentTimeMillis();
        
        System.out.println(">> 注册流程耗时: " + (endTime - startTime) + "ms");
        
        // 等待邮件发送完成
        Thread.sleep(3000);
    }
}
