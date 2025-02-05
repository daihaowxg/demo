package com.crhms.cdmp.ds.service;

import com.crhms.cdmp.ds.aop.DS;
import com.crhms.cdmp.ds.domain.User;
import com.crhms.cdmp.ds.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wxg
 * @since 2025/2/5
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public List<User> getUsers() {
        return userMapper.selectList(null);
    }

    @DS("slave")
    public List<User> getUsersFromSlave() {
        return userMapper.selectList(null);
    }
}
