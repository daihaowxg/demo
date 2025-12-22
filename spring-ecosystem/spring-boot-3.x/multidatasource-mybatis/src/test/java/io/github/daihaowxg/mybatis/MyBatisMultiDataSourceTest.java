package io.github.daihaowxg.mybatis;

import io.github.daihaowxg.mybatis.entity.User;
import io.github.daihaowxg.mybatis.service.MyBatisMultiDataSourceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MyBatis 多数据源测试
 *
 * @author daihaowxg
 */
@Slf4j
@SpringBootTest
class MyBatisMultiDataSourceTest {

    @Autowired
    private MyBatisMultiDataSourceService service;

    @Test
    @DisplayName("测试从主数据源查询用户")
    void testFindAllFromPrimary() {
        log.info("\n=== 测试从主数据源查询用户 ===");

        List<User> users = service.findAllFromPrimary();

        log.info("主数据源用户列表:");
        users.forEach(user -> log.info("  - {}", user));

        assertThat(users).isNotEmpty();
    }

    @Test
    @DisplayName("测试从第二个数据源查询用户")
    void testFindAllFromSecondary() {
        log.info("\n=== 测试从第二个数据源查询用户 ===");

        List<User> users = service.findAllFromSecondary();

        log.info("第二个数据源用户列表:");
        users.forEach(user -> log.info("  - {}", user));

        assertThat(users).isNotEmpty();
    }

    @Test
    @DisplayName("测试保存用户到主数据源")
    void testSaveUserToPrimary() {
        log.info("\n=== 测试保存用户到主数据源 ===");

        User user = new User();
        user.setName("测试用户-主库");
        user.setEmail("test@primary.com");

        User savedUser = service.saveUserToPrimary(user);

        log.info("保存的用户: {}", savedUser);
        assertThat(savedUser.getId()).isNotNull();

        // 验证保存成功
        Optional<User> found = service.findByIdFromPrimary(savedUser.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("测试用户-主库");
    }

    @Test
    @DisplayName("测试保存用户到第二个数据源")
    void testSaveUserToSecondary() {
        log.info("\n=== 测试保存用户到第二个数据源 ===");

        User user = new User();
        user.setName("测试用户-从库");
        user.setEmail("test@secondary.com");

        User savedUser = service.saveUserToSecondary(user);

        log.info("保存的用户: {}", savedUser);
        assertThat(savedUser.getId()).isNotNull();

        // 验证保存成功
        Optional<User> found = service.findByIdFromSecondary(savedUser.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("测试用户-从库");
    }

    @Test
    @DisplayName("测试更新主数据源中的用户")
    void testUpdateUserInPrimary() {
        log.info("\n=== 测试更新主数据源中的用户 ===");

        // 先插入一个用户
        User user = new User();
        user.setName("原始名称");
        user.setEmail("original@primary.com");
        service.saveUserToPrimary(user);

        // 更新用户
        user.setName("更新后的名称");
        user.setEmail("updated@primary.com");
        int updated = service.updateUserInPrimary(user);

        assertThat(updated).isEqualTo(1);

        // 验证更新成功
        Optional<User> found = service.findByIdFromPrimary(user.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("更新后的名称");
        assertThat(found.get().getEmail()).isEqualTo("updated@primary.com");
    }

    @Test
    @DisplayName("测试跨数据源同步用户")
    void testSyncUserFromPrimaryToSecondary() {
        log.info("\n=== 测试跨数据源同步用户 ===");

        // 在主数据源创建用户
        User user = new User();
        user.setName("待同步用户");
        user.setEmail("sync@primary.com");
        User savedUser = service.saveUserToPrimary(user);

        log.info("主数据源用户: {}", savedUser);

        // 同步到第二个数据源
        service.syncUserFromPrimaryToSecondary(savedUser.getId());

        // 验证第二个数据源中存在该用户（名称和邮箱相同，但 ID 不同）
        List<User> secondaryUsers = service.findAllFromSecondary();
        boolean found = secondaryUsers.stream()
                .anyMatch(u -> u.getName().equals("待同步用户")
                        && u.getEmail().equals("sync@primary.com"));

        assertThat(found).isTrue();
        log.info("用户已成功同步到第二个数据源");
    }

    @Test
    @DisplayName("测试获取两个数据源的用户统计")
    void testGetUserCountSummary() {
        log.info("\n=== 测试获取两个数据源的用户统计 ===");

        String summary = service.getUserCountSummary();

        log.info("统计结果: {}", summary);
        assertThat(summary).contains("主数据源用户数");
        assertThat(summary).contains("第二个数据源用户数");
    }

    @Test
    @DisplayName("测试删除用户")
    void testDeleteUser() {
        log.info("\n=== 测试删除用户 ===");

        // 在主数据源创建用户
        User user = new User();
        user.setName("待删除用户");
        user.setEmail("delete@primary.com");
        User savedUser = service.saveUserToPrimary(user);

        log.info("创建的用户: {}", savedUser);

        // 删除用户
        int deleted = service.deleteUserFromPrimary(savedUser.getId());
        assertThat(deleted).isEqualTo(1);

        // 验证已删除
        Optional<User> found = service.findByIdFromPrimary(savedUser.getId());
        assertThat(found).isEmpty();
        log.info("用户已成功删除");
    }

    @Test
    @DisplayName("测试批量同步所有用户")
    void testSyncAllUsers() {
        log.info("\n=== 测试批量同步所有用户 ===");

        // 在主数据源添加几个用户
        for (int i = 1; i <= 3; i++) {
            User user = new User();
            user.setName("批量用户" + i);
            user.setEmail("batch" + i + "@primary.com");
            service.saveUserToPrimary(user);
        }

        // 批量同步
        int syncCount = service.syncAllUsersFromPrimaryToSecondary();

        log.info("同步了 {} 个用户", syncCount);
        assertThat(syncCount).isGreaterThan(0);

        // 验证第二个数据源中的用户数量增加
        String summary = service.getUserCountSummary();
        log.info("同步后的统计: {}", summary);
    }
}
