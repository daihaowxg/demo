package com.crhms.cdmp.user.controller;

import com.crhms.cdmp.user.domain.User;
import com.crhms.cdmp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wxg
 * @since 2025/2/5
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("slave")
    public List<User> getUsersFromSlave() {
        return userService.getUsersFromSlave();
    }

    @GetMapping("slave2")
    public List<User> getUsersFromSlave2() {
        return userService.getUsersFromSlave2();
    }

    @GetMapping("change")
    public void changeDataSource() {
        userService.changeDataSource();
    }

}
