package io.github.daihaowxg.junit.junit5;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 断言与异常测试示例
 */
public class AssertionTest {

    @Test
    void testAsserts() {
        // 基础断言 (注意：message 参数移到了最后)
        assertEquals(4, 2 + 2, "2 + 2 应该等于 4");
        assertTrue(5 > 3, () -> "条件应为真 (Lambda 懒加载消息)");
        
        // 组合断言：所有断言都会执行，最后一起报告失败
        assertAll("user",
            () -> assertEquals("John", "John"),
            () -> assertEquals("Doe", "Doe")
        );
    }

    /**
     * JUnit 5 异常测试方式: assertThrows
     * 优点：
     * 1. 可以精确断言代码块抛出异常
     * 2. 可以捕获异常对象，进一步验证异常消息
     */
    @Test
    void testException() {
        List<String> list = new ArrayList<>();
        
        IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            list.get(0);
        });
        
        // 验证异常消息 (注意：不同 JDK 版本消息可能不同，这里匹配 JDK 17+)
        assertEquals("Index 0 out of bounds for length 0", exception.getMessage());
    }
}
