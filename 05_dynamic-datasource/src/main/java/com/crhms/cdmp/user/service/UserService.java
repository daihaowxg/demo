package com.crhms.cdmp.user.service;

import com.crhms.cdmp.ds.aop.DS;
import com.crhms.cdmp.ds.manager.DynamicDataSourceContextHolder;
import com.crhms.cdmp.user.domain.User;
import com.crhms.cdmp.user.mapper.UserMapper;
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

    public List<User> getUsersFromSlave2() {
        DynamicDataSourceContextHolder.setDataSourceKey("slave");
        return userMapper.selectList(null);
    }

    public void changeDataSource() {
        DynamicDataSourceContextHolder.setDataSourceKey("slave");
        List<User> slaveUsers = userMapper.selectList(null);
        System.out.println("\nslave 数据源的用户：");
        System.out.println(slaveUsers);

        DynamicDataSourceContextHolder.setDataSourceKey("master");
        List<User> masterUsers = userMapper.selectList(null);
        System.out.println("\nmaster 数据源的用户：");
        System.out.println(masterUsers);
    }
}
