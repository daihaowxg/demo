package io.github.daihaowxg.openrewrite.legacy;

import org.junit.jupiter.api.*;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 4 测试类
 * 用于演示 OpenRewrite 自动迁移到 JUnit 5
 */
public class JUnit4Test {

    private UnformattedCode testObject;

    @BeforeAll
    public static void setUpClass() {
        System.out.println("Setting up test class...");
    }

    @AfterAll
    public static void tearDownClass() {
        System.out.println("Tearing down test class...");
    }

    @BeforeEach
    public void setUp() {
        testObject = new UnformattedCode("John", 25,
            java.util.Arrays.asList("Reading", "Gaming"));
    }

    @AfterEach
    public void tearDown() {
        testObject = null;
    }

    @Test
    public void testIsAdult() {
        // JUnit 4 断言
        assertTrue(testObject.isAdult());
        assertTrue(testObject.getAge() == 25, "Age should be 25");
    }

    @Test
    public void testGetName() {
        assertEquals("John", testObject.getName());
        assertNotNull(testObject.getName());
    }

    @Test
    public void testHasHobby() {
        assertTrue(testObject.hasHobby("Reading"));
        assertFalse(testObject.hasHobby("Swimming"));
    }

    @Test
    public void testGetHobbyCount() {
        assertEquals(2, testObject.getHobbyCount());
    }

    @Test
    public void testGetUpperCaseHobbies() {
        var hobbies = testObject.getUpperCaseHobbies();
        assertNotNull(hobbies);
        assertEquals(2, hobbies.size());
        assertTrue(hobbies.contains("READING"));
        assertTrue(hobbies.contains("GAMING"));
    }

    @Test
    public void testNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            testObject.setHobbies(null);
            testObject.getUpperCaseHobbies();
        });
    }

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    public void testPerformance() {
        // 应该在 1 秒内完成
        testObject.getDescription();
    }

    @Disabled("This test is temporarily disabled")
    @Test
    public void testIgnored() {
        fail("This test should not run");
    }

    @Test
    public void testCalculateSomething() {
        Integer result = testObject.calculateSomething(5, 3);
        assertEquals(Integer.valueOf(8), result);
    }
}
