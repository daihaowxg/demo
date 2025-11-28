package io.github.daihaowxg.junit.junit4;

import org.junit.Assume;
import org.junit.Test;

/**
 * JUnit 4 假设 (Assumption) 示例
 * <p>
 * 假设失败时，测试会被标记为 Ignored (跳过)，而不是 Failed。
 */
public class AssumptionTest {

    @Test
    public void testAssumeTrue() {
        // 假设当前是开发环境
        boolean isDevEnv = "dev".equals(System.getProperty("env", "dev"));
        
        // 如果假设不成立，测试中止并被忽略
        Assume.assumeTrue("不是开发环境，跳过测试", isDevEnv);
        
        System.out.println("环境检查通过，执行测试...");
    }
}
