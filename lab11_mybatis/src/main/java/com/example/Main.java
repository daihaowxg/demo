package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.mapper") // 扫描 Mapper 接口
public class Main {

    // @Autowired
    // private AuthorMapper authorMapper;
    //
    //
    // @Bean
    // public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    //     return args -> {
    //         // 插入一条初始数据用于测试
    //         Author initialAuthor = new Author();
    //         initialAuthor.setId(1);
    //         initialAuthor.setUsername("testuser");
    //         initialAuthor.setPassword("123456");
    //         initialAuthor.setEmail("test@example.com");
    //         initialAuthor.setBio("Initial bio");
    //         authorMapper.insertAuthor(initialAuthor);
    //
    //         System.out.println("✅ 初始数据已插入: " + authorMapper.findAuthorById(1));
    //         System.out.println("------------------------------------------------------");
    //
    //         // 创建一个更新对象，但所有可更新字段都为 null
    //         Author authorToUpdate = new Author();
    //         authorToUpdate.setId(1); // 只有 id 有值
    //         authorToUpdate.setUsername(null);
    //         authorToUpdate.setPassword(null);
    //         authorToUpdate.setEmail(null);
    //         authorToUpdate.setBio(null);
    //
    //         System.out.println("🚀 准备执行更新，但所有待更新字段均为 null...");
    //         System.out.println("👀 请观察下方 MyBatis 的日志，是否会生成 UPDATE SQL 语句...");
    //
    //         // 执行更新操作
    //         int affectedRows = authorMapper.updateAuthorIfNecessary(authorToUpdate);
    //
    //         System.out.println("------------------------------------------------------");
    //         System.out.println("🎉 更新操作执行完毕!");
    //         System.out.println("👉 影响的行数 (Affected Rows): " + affectedRows);
    //
    //         // 验证结果
    //         Author authorAfterUpdate = authorMapper.findAuthorById(1);
    //         System.out.println("🔍 更新后的数据: " + authorAfterUpdate);
    //         System.out.println("💡 结论: 数据没有被改变，MyBatis 没有生成无效的 SQL，也没有报错。");
    //     };
    // }


    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }



}