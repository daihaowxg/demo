package io.github.daihaowxg.event;

import io.github.daihaowxg.event.traditional.TraditionalUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Spring 事件机制测试
 * <p>
 * 测试用户注册事件的发布和监听机制
 * 验证多个监听器的执行顺序和条件监听功能
 *
 * @author wxg
 */
@SpringBootTest
@DisplayName("Spring 事件机制测试")
class SpringEventTest {

    @Autowired
    private UserService userService;

    /**
     * 测试普通用户注册
     * <p>
     * 验证事件发布后，所有监听器按顺序执行：
     * 1. EmailListener - 发送欢迎邮件
     * 2. PointsListener - 赠送新人积分
     * 3. StatisticsListener - 记录注册统计
     */
    @Test
    @DisplayName("测试普通用户注册 - 验证多个监听器按顺序执行")
    void testNormalUserRegister() {
        System.out.println("\n========== 测试普通用户注册 ==========");
        userService.register("daihaowxg");
    }

    /**
     * 测试 VIP 用户注册
     * <p>
     * 验证条件监听功能，VIP 用户（用户名以 vip_ 开头）会额外触发：
     * 4. VipUserListener - 赠送 VIP 专属礼包
     */
    @Test
    @DisplayName("测试 VIP 用户注册 - 验证条件监听器触发")
    void testVipUserRegister() {
        System.out.println("\n========== 测试 VIP 用户注册 ==========");
        userService.register("vip_alice");
    }

    @Autowired
    private TraditionalUserService traditionalUserService;

    /**
     * 测试传统方式的用户注册（不使用事件机制）
     * <p>
     * 对比展示传统方式的问题：
     * 1. 高耦合：UserService 需要依赖所有后续服务
     * 2. 难扩展：添加新功能需要修改 UserService
     * 3. 违反开闭原则
     */
    @Test
    @DisplayName("对比：传统方式 - 展示高耦合问题")
    void testTraditionalWay() {
        System.out.println("\n========== 传统方式（高耦合） ==========");
        traditionalUserService.register("traditional_user");
        System.out.println(">> 问题：UserService 必须依赖并调用所有后续服务");
    }

    /**
     * 测试异步事件监听
     * <p>
     * 验证异步处理的优势：
     * 1. 不阻塞主流程，立即返回
     * 2. 耗时操作在后台执行
     * 3. 提升用户体验
     */
    @Test
    @DisplayName("对比：异步监听 - 展示异步处理优势")
    void testAsyncEventListener() throws InterruptedException {
        System.out.println("\n========== 异步事件监听 ==========");
        long startTime = System.currentTimeMillis();

        userService.register("async_user");

        long endTime = System.currentTimeMillis();
        System.out.println(">> 主流程耗时: " + (endTime - startTime) + "ms (几乎不受异步操作影响)");
        System.out.println(">> 异步邮件正在后台发送中...");

        // 等待异步操作完成（仅用于演示）
        Thread.sleep(3000);
        System.out.println(">> 异步操作已完成");
    }
}
