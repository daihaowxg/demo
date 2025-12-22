-- 第二个数据源初始化脚本

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);

-- 插入测试数据
INSERT INTO users (name, email) VALUES ('David', 'david@secondary.com');
INSERT INTO users (name, email) VALUES ('Eve', 'eve@secondary.com');
