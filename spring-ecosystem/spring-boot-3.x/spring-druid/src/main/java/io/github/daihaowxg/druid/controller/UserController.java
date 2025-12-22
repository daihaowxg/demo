package io.github.daihaowxg.druid.controller;

import io.github.daihaowxg.druid.entity.User;
import io.github.daihaowxg.druid.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 * 提供 RESTful API 接口
 *
 * @author daihaowxg
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    /**
     * 获取所有用户
     *
     * @return 用户列表
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    
    /**
     * 根据ID获取用户
     *
     * @param id 用户ID
     * @return 用户对象
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
    
    /**
     * 搜索用户
     *
     * @param username 用户名
     * @return 用户列表
     */
    @GetMapping("/search")
    public List<User> searchUsers(@RequestParam String username) {
        return userService.searchByUsername(username);
    }
    
    /**
     * 创建用户
     *
     * @param user 用户对象
     * @return 是否成功
     */
    @PostMapping
    public boolean createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
    
    /**
     * 更新用户
     *
     * @param id 用户ID
     * @param user 用户对象
     * @return 是否成功
     */
    @PutMapping("/{id}")
    public boolean updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return userService.updateUser(user);
    }
    
    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
    
    /**
     * 获取用户总数
     *
     * @return 用户总数
     */
    @GetMapping("/count")
    public long getUserCount() {
        return userService.getUserCount();
    }
    
    /**
     * 获取 Druid 连接池统计信息
     *
     * @return 统计信息
     */
    @GetMapping("/druid/stats")
    public String getDruidStats() {
        return userService.getDruidStatistics();
    }
}
