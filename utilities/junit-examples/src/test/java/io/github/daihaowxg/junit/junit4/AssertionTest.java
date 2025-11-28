package io.github.daihaowxg.junit.junit4;

import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * JUnit 4 断言与异常测试示例
 */
public class AssertionTest {

    @Test
    public void testAsserts() {
        // 基础断言
        assertEquals(4, 2 + 2);
        assertTrue("条件应为真", 5 > 3);
        assertFalse(5 < 3);
        assertNull(null);
        assertNotNull(new Object());
        
        // 数组断言
        assertArrayEquals(new int[]{1, 2}, new int[]{1, 2});
    }

    /**
     * JUnit 4 异常测试方式 1: 使用 @Test(expected = ...)
     * 缺点：无法验证异常的消息内容，且只要方法内抛出该异常即算通过（无法精确到某行代码）
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testException() {
        List<String> list = new ArrayList<>();
        list.get(0); // 抛出 IndexOutOfBoundsException
    }
}
