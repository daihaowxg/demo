-- 主数据源初始化脚本

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);

-- 插入测试数据
INSERT INTO users (name, email) VALUES ('Alice', 'alice@primary.com');
INSERT INTO users (name, email) VALUES ('Bob', 'bob@primary.com');
INSERT INTO users (name, email) VALUES ('Charlie', 'charlie@primary.com');
