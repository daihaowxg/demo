package io.github.daihaowxg.demo.lab16springcache.controller;

import io.github.daihaowxg.demo.lab16springcache.entity.User;
import io.github.daihaowxg.demo.lab16springcache.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户控制器 - 提供 REST API 测试缓存功能
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 创建用户
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    /**
     * 根据 ID 查询用户
     * 第一次查询会执行数据库操作（观察日志）
     * 第二次查询会从缓存获取（观察日志）
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * 根据邮箱查询用户
     */
    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    /**
     * 更新用户
     * 会同时更新缓存
     */
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return userService.updateUser(user);
    }

    /**
     * 删除用户
     * 会清除缓存
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    /**
     * 刷新用户缓存
     */
    @PostMapping("/{id}/refresh")
    public User refreshUser(@PathVariable Long id) {
        return userService.refreshUser(id);
    }

    /**
     * 清除所有用户缓存
     */
    @DeleteMapping("/cache")
    public void clearAllCache() {
        userService.clearAllCache();
    }

    /**
     * 获取所有用户（不走缓存，用于验证数据）
     */
    @GetMapping
    public Map<Long, User> getAllUsers() {
        return userService.getAllUsers();
    }
}
