package io.github.daihaowxg.junit.junit5;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * JUnit 5 假设 (Assumption) 示例
 */
public class AssumptionTest {

    @Test
    void testAssumeTrue() {
        // 假设当前是开发环境
        boolean isDevEnv = "dev".equals(System.getProperty("env", "dev"));
        
        // 如果假设不成立，测试中止并被标记为 Aborted (跳过)
        assumeTrue(isDevEnv, "不是开发环境，跳过测试");
        
        System.out.println("环境检查通过，执行测试...");
    }

    @Test
    void testAssumingThat() {
        // assumingThat 允许在特定条件下执行代码块，但不影响后续代码执行
        // 即使假设失败，测试也不会中止，只是 lambda 块不执行
        assumingThat("CI".equals(System.getProperty("env")), () -> {
            // 仅在 CI 环境执行的断言
            assertEquals(2, 1 + 1);
        });

        // 无论假设是否成立，这里都会执行
        assertEquals(4, 2 + 2);
    }
}
