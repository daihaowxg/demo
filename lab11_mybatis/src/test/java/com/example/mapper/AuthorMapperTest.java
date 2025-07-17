package com.example.mapper;

import com.example.model.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthorMapperTest {

    @Autowired
    private AuthorMapper authorMapper;

    @Test
    void updateAuthorIfNecessary() {
        // 插入一条初始数据用于测试
        Author initialAuthor = new Author();
        initialAuthor.setId(1);
        initialAuthor.setUsername("testuser");
        initialAuthor.setPassword("123456");
        initialAuthor.setEmail("test@example.com");
        initialAuthor.setBio("Initial bio");
        authorMapper.insertAuthor(initialAuthor);

        System.out.println("✅ 初始数据已插入: " + authorMapper.findAuthorById(1));
        System.out.println("------------------------------------------------------");

        // 创建一个更新对象，但所有可更新字段都为 null
        Author authorToUpdate = new Author();
        authorToUpdate.setId(1); // 只有 id 有值
        authorToUpdate.setUsername("zhangsan");
        authorToUpdate.setPassword(null);
        authorToUpdate.setEmail(null);
        authorToUpdate.setBio(null);

        System.out.println("🚀 准备执行更新，部分待更新字段为 null...");
        System.out.println("👀 请观察下方 MyBatis 的日志，生成的 UPDATE SQL 语句...");

        // 执行更新操作
        int affectedRows = authorMapper.updateAuthorIfNecessary(authorToUpdate);

        System.out.println("------------------------------------------------------");
        System.out.println("🎉 更新操作执行完毕!");
        System.out.println("👉 影响的行数 (Affected Rows): " + affectedRows);

        // 验证结果
        Author authorAfterUpdate = authorMapper.findAuthorById(1);
        System.out.println("🔍 更新后的数据: " + authorAfterUpdate);
        System.out.println("💡 结论: MyBatis 会动态地生成 set 语句，并且会处理好后缀的逗号。");
    }
}