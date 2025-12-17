package io.github.daihaowxg.spring.jdbc.repository;

import io.github.daihaowxg.spring.jdbc.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class JdbcRepositoryTest {

    @Autowired
    private StandardJdbcRepository standardJdbcRepository;

    @Autowired
    private SpringJdbcRepository springJdbcRepository;

    @Autowired
    private io.github.daihaowxg.spring.jdbc.mapper.UserMapper userMapper;

    @Test
    void testStandardJdbcRepository() {
        testRepository(standardJdbcRepository);
    }

    @Test
    void testSpringJdbcRepository() {
        testRepository(springJdbcRepository);
    }

    @Test
    void testMyBatisMapper() {
        testRepository(userMapper);
    }

    private void testRepository(Object repository) {
        User user = new User(null, "Test User", "test@example.com");
        User savedUser;

        if (repository instanceof StandardJdbcRepository) {
            savedUser = ((StandardJdbcRepository) repository).save(user);
        } else if (repository instanceof SpringJdbcRepository) {
            savedUser = ((SpringJdbcRepository) repository).save(user);
        } else {
             ((io.github.daihaowxg.spring.jdbc.mapper.UserMapper) repository).save(user);
             savedUser = user;
        }

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("Test User");

        Optional<User> foundUser;
        if (repository instanceof StandardJdbcRepository) {
            foundUser = ((StandardJdbcRepository) repository).findById(savedUser.getId());
        } else if (repository instanceof SpringJdbcRepository) {
            foundUser = ((SpringJdbcRepository) repository).findById(savedUser.getId());
        } else {
            foundUser = ((io.github.daihaowxg.spring.jdbc.mapper.UserMapper) repository).findById(savedUser.getId());
        }

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Test User");

        List<User> allUsers;
        if (repository instanceof StandardJdbcRepository) {
            allUsers = ((StandardJdbcRepository) repository).findAll();
        } else if (repository instanceof SpringJdbcRepository) {
            allUsers = ((SpringJdbcRepository) repository).findAll();
        } else {
            allUsers = ((io.github.daihaowxg.spring.jdbc.mapper.UserMapper) repository).findAll();
        }
        assertThat(allUsers).isNotEmpty();

        savedUser.setName("Updated User");
        if (repository instanceof StandardJdbcRepository) {
            ((StandardJdbcRepository) repository).update(savedUser);
        } else if (repository instanceof SpringJdbcRepository) {
            ((SpringJdbcRepository) repository).update(savedUser);
        } else {
            ((io.github.daihaowxg.spring.jdbc.mapper.UserMapper) repository).update(savedUser);
        }

        if (repository instanceof StandardJdbcRepository) {
            foundUser = ((StandardJdbcRepository) repository).findById(savedUser.getId());
        } else if (repository instanceof SpringJdbcRepository) {
            foundUser = ((SpringJdbcRepository) repository).findById(savedUser.getId());
        } else {
            foundUser = ((io.github.daihaowxg.spring.jdbc.mapper.UserMapper) repository).findById(savedUser.getId());
        }
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Updated User");

        if (repository instanceof StandardJdbcRepository) {
            ((StandardJdbcRepository) repository).deleteById(savedUser.getId());
            foundUser = ((StandardJdbcRepository) repository).findById(savedUser.getId());
        } else if (repository instanceof SpringJdbcRepository) {
            ((SpringJdbcRepository) repository).deleteById(savedUser.getId());
            foundUser = ((SpringJdbcRepository) repository).findById(savedUser.getId());
        } else {
            ((io.github.daihaowxg.spring.jdbc.mapper.UserMapper) repository).deleteById(savedUser.getId());
            foundUser = ((io.github.daihaowxg.spring.jdbc.mapper.UserMapper) repository).findById(savedUser.getId());
        }
        assertThat(foundUser).isEmpty();
    }
}
