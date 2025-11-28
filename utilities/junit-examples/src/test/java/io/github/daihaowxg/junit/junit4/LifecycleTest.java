package io.github.daihaowxg.junit.junit4;

import org.junit.*;

/**
 * JUnit 4 生命周期示例
 * <p>
 * 展示核心注解：
 * <ul>
 *     <li>@BeforeClass: 所有测试开始前执行 (必须是 static)</li>
 *     <li>@AfterClass: 所有测试结束后执行 (必须是 static)</li>
 *     <li>@Before: 每个测试方法前执行</li>
 *     <li>@After: 每个测试方法后执行</li>
 *     <li>@Test: 测试方法</li>
 *     <li>@Ignore: 忽略测试</li>
 * </ul>
 */
public class LifecycleTest {

    @BeforeClass
    public static void setupClass() {
        System.out.println("@BeforeClass - 所有测试开始前执行一次");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("@AfterClass - 所有测试结束后执行一次");
    }

    @Before
    public void setup() {
        System.out.println("  @Before - 每个测试开始前执行");
    }

    @After
    public void tearDown() {
        System.out.println("  @After - 每个测试结束后执行");
    }

    @Test
    public void testMethod1() {
        System.out.println("    执行测试方法 1");
    }

    @Test
    public void testMethod2() {
        System.out.println("    执行测试方法 2");
    }

    @Ignore("展示 @Ignore 用法")
    @Test
    public void testIgnored() {
        System.out.println("    这个测试不会被执行");
    }
}
