-- 第二个数据源初始化脚本

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100)
);

-- 插入测试数据
INSERT INTO users (name, email) VALUES ('赵六', 'zhaoliu@secondary.com');
INSERT INTO users (name, email) VALUES ('钱七', 'qianqi@secondary.com');
