package io.github.daihaowxg.event.traditional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户服务（传统方式 - 不使用事件机制）
 * <p>
 * 缺点：
 * 1. 高耦合：UserService 需要依赖所有后续服务
 * 2. 难扩展：添加新功能需要修改此类，违反开闭原则
 * 3. 难测试：需要 mock 所有依赖的服务
 * 4. 职责不清：UserService 需要知道所有后续操作
 *
 * @author wxg
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TraditionalUserService {

    // 需要依赖所有后续服务 - 高耦合！
    private final EmailService emailService;
    private final PointsService pointsService;
    private final StatisticsService statisticsService;

    /**
     * 用户注册（传统方式）
     * <p>
     * 问题：UserService 必须知道并调用所有后续操作
     * 如果要添加新功能（如发送短信），必须修改这个方法
     *
     * @param username 用户名
     */
    public void register(String username) {
        log.info("执行用户注册逻辑: {}", username);
        
        // 执行注册核心逻辑
        System.out.println(">> 用户 " + username + " 注册成功");

        // 必须手动调用所有后续操作 - 紧密耦合！
        emailService.sendWelcomeEmail(username);
        pointsService.grantNewUserPoints(username);
        statisticsService.recordRegistration(username);
        
        // 如果要添加新功能，必须在这里添加代码 - 违反开闭原则！
        // smsService.sendWelcomeSms(username);
    }
}
