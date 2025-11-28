package io.github.daihaowxg.junit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * 来源：<a href="https://www.baeldung.com/junit-5">...</a>
 */
@Slf4j
public class JUnitTest {

    /*
     * 可以看到如下日志打印：每个测试方法前后都有 {@code @BeforeEach} 和 {@code @AfterEach} 的日志，最开头和最结尾是 {@code @BeforeAll} 和 {@code @AfterAll} 的日志。
     *
     * 18:30:24.844 [main] INFO com.example.junit.JUnitTest - @BeforeAll - 在所有测试方法之前执行一次
     *
     * 这里用来说明为什么第一个测试方法被 disabled 了
     *
     * 这里用来说明为什么第二个测试方法被 disabled 了
     * 18:30:24.854 [main] INFO com.example.junit.JUnitTest - @BeforeEach - 在每个测试方法之前都会执行一次
     * 18:30:24.855 [main] INFO com.example.junit.JUnitTest - 第二个成功的测试方法
     * 18:30:24.856 [main] INFO com.example.junit.JUnitTest - @AfterEach - 每个测试方法之后都会执行一次
     * 18:30:24.859 [main] INFO com.example.junit.JUnitTest - @BeforeEach - 在每个测试方法之前都会执行一次
     * 18:30:24.859 [main] INFO com.example.junit.JUnitTest - 第一个成功的测试方法
     * 18:30:24.859 [main] INFO com.example.junit.JUnitTest - @AfterEach - 每个测试方法之后都会执行一次
     * 18:30:24.860 [main] INFO com.example.junit.JUnitTest - @AfterAll - 在所有测试方法之后执行一次
     */

    /**
     * {@code @BeforeAll} 注解用于标记一个方法，该方法将在所有测试方法执行之前运行一次。
     * <p>需要注意的是，带有@BeforeAll 注解的方法必须是静态的，否则代码将无法编译。</p>
     */
    @BeforeAll
    static void setup() {
        log.info("@BeforeAll - 在所有测试方法之前执行一次");
    }

    /**
     * {@code @BeforeEach} 注解用于标记一个方法，该方法将在每个测试方法执行之前运行。
     */
    @BeforeEach
    void init() {
        log.info("@BeforeEach - 在每个测试方法之前都会执行一次");
    }

    /**
     * {@code @AfterEach} 注解用于标记一个方法，该方法将在每个测试方法执行之后运行。
     */
    @AfterEach
    void tearDown() {
        log.info("@AfterEach - 每个测试方法之后都会执行一次");
    }

    /**
     * {@code @AfterAll} 注解用于标记一个方法，该方法将在所有测试方法执行之后运行一次。
     * 请注意，带有@AfterAll 注解的方法也必须是静态方法。
     */
    @AfterAll
    static void done() {
        log.info("@AfterAll - 在所有测试方法之后执行一次");
    }

    /**
     * {@code @Test} 注解用于标记一个方法为测试方法。
     * JUnit 将会执行所有带有 @Test 注解的方法。
     * {@code @DisplayName} 注解用于为测试方法提供一个可读的名称，
     */
    @Test
    @DisplayName("展示的名称")
    void testSingleSuccessTest() {
        log.info("第一个成功的测试方法");
    }

    @Test
    @DisplayName("展示的名称")
    void testSingleSuccessTest2() {
        log.info("第二个成功的测试方法");
    }

    /**
     * {@code @Disabled} 注解用于标记一个测试方法为禁用状态，
     * JUnit 将不会执行被 @Disabled 注解标记的方法。
     * 可以在注解中添加一个字符串参数来说明为什么该测试方法被禁用。
     */
    @Test
    @Disabled("这里用来说明为什么第一个测试方法被 disabled 了")
    void testShowSomething1() {
    }

    @Test
    @Disabled("这里用来说明为什么第二个测试方法被 disabled 了")
    void testShowSomething2() {
    }


    /**
     * JUnit 5 支持使用 Lambda 表达式来编写断言。
     * 这种方式可以让代码更加简洁，并且在断言失败时提供更好的错误信息。
     */
    @Test
    void lambdaExpressions() {
        List<Integer> numbers = Arrays.asList(1, 2, 3);
        // 断言列表中的数字总和大于 5，否则打印后边的消息
        assertTrue(numbers.stream()
                .mapToInt(Integer::intValue)
                .sum() > 5, () -> "Sum should be greater than 5");
    }

    /**
     * JUnit 5 支持使用 {@code assertAll} 方法来组合多个断言。
     */
    @Test
    void groupAssertions() {
        int[] numbers = {0, 1, 2, 3, 1};
        assertAll("numbers",
                // 判断数组中的每个元素是否符合预期，第一个参数是期待的值，第二个参数是实际的值
                () -> assertEquals(1, numbers[1]),
                () -> assertEquals(3, numbers[3]),
                () -> assertEquals(1, numbers[4])
        );
    }


    @Test
    void trueAssumption() {
        assumeTrue(5 > 1, "assumption is not true");
        // assumeTrue(false, "assumption is not true");
        assertEquals(5 + 2, 7);
    }

    @Test
    void falseAssumption() {
        assumeFalse(5 < 1);
        assertEquals(5 + 2, 7);
    }

    @Test
    void assumptionThat() {
        String someString = "Just a string";
        assumingThat(
                someString.equals("Just a string"),
                () -> assertEquals(2 + 2, 4)
        );
    }


}
