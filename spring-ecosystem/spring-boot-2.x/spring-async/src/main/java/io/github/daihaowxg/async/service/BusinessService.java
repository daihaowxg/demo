package io.github.daihaowxg.async.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * 业务服务示例
 * <p>
 * 演示如何在普通业务方法中调用异步方法
 *
 * @author wxg
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessService {

    private final AsyncService asyncService;

    /**
     * 用户注册业务
     * <p>
     * 注册完成后，异步发送欢迎邮件
     * 主流程不等待邮件发送完成
     */
    public void registerUser(String username, String email) {
        log.info("开始注册用户: {}", username);
        
        // 同步执行：保存用户到数据库
        saveUserToDatabase(username, email);
        log.info("用户注册成功: {}", username);
        
        // 异步执行：发送欢迎邮件（不阻塞主流程）
        asyncService.sendEmailAsync(email, "欢迎注册");
        
        log.info("注册流程完成，邮件正在后台发送");
    }

    /**
     * 查询用户详情
     * <p>
     * 演示如何获取异步方法的返回值
     */
    public void getUserDetails(Long userId) throws Exception {
        log.info("开始查询用户详情: {}", userId);
        
        // 调用异步方法，获取 Future
        CompletableFuture<String> future = asyncService.getUserInfoAsync(userId);
        
        // 可以继续执行其他操作
        log.info("异步查询已提交，继续执行其他操作...");
        
        // 需要结果时，调用 get() 等待
        String userInfo = future.get();
        log.info("获取到用户信息: {}", userInfo);
    }

    /**
     * 批量处理订单
     * <p>
     * 演示批量异步处理的场景
     */
    public void processBatchOrders(Long[] orderIds) {
        log.info("开始批量处理订单，数量: {}", orderIds.length);
        
        // 每个订单异步处理，并发执行
        for (Long orderId : orderIds) {
            asyncService.processOrderAsync(orderId);
        }
        
        log.info("所有订单已提交异步处理");
    }

    private void saveUserToDatabase(String username, String email) {
        // 模拟数据库操作
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
