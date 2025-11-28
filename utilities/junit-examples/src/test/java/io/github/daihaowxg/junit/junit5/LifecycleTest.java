package io.github.daihaowxg.junit.junit5;

import org.junit.jupiter.api.*;

/**
 * JUnit 5 (Jupiter) 生命周期示例
 * <p>
 * 核心注解变更：
 * <ul>
 *     <li>@BeforeAll (替代 @BeforeClass): 所有测试前执行 (必须 static)</li>
 *     <li>@AfterAll (替代 @AfterClass): 所有测试后执行 (必须 static)</li>
 *     <li>@BeforeEach (替代 @Before): 每个测试前执行</li>
 *     <li>@AfterEach (替代 @After): 每个测试后执行</li>
 *     <li>@Test: 测试方法 (包路径变更 org.junit.jupiter.api.Test)</li>
 *     <li>@Disabled (替代 @Ignore): 禁用测试</li>
 *     <li>@DisplayName: 自定义展示名称</li>
 * </ul>
 */
@DisplayName("JUnit 5 生命周期演示")
public class LifecycleTest {

    @BeforeAll
    static void setupAll() {
        System.out.println("@BeforeAll - 所有测试开始前执行一次");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("@AfterAll - 所有测试结束后执行一次");
    }

    @BeforeEach
    void setup() {
        System.out.println("  @BeforeEach - 每个测试开始前执行");
    }

    @AfterEach
    void tearDown() {
        System.out.println("  @AfterEach - 每个测试结束后执行");
    }

    @Test
    @DisplayName("测试方法 1 (自定义名称)")
    void testMethod1() {
        System.out.println("    执行测试方法 1");
    }

    @Test
    void testMethod2() {
        System.out.println("    执行测试方法 2");
    }

    @Disabled("展示 @Disabled 用法")
    @Test
    void testDisabled() {
        System.out.println("    这个测试不会被执行");
    }
}
