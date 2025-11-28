package io.github.daihaowxg.openrewrite.legacy;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.*;

/**
 * JUnit 4 测试类
 * 用于演示 OpenRewrite 自动迁移到 JUnit 5
 */
public class JUnit4Test {

    private UnformattedCode testObject;

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Setting up test class...");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("Tearing down test class...");
    }

    @Before
    public void setUp() {
        testObject = new UnformattedCode("John", 25,
            java.util.Arrays.asList("Reading", "Gaming"));
    }

    @After
    public void tearDown() {
        testObject = null;
    }

    @Test
    public void testIsAdult() {
        // JUnit 4 断言
        assertTrue(testObject.isAdult());
        assertTrue("Age should be 25", testObject.getAge() == 25);
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

    @Test(expected = NullPointerException.class)
    public void testNullPointerException() {
        testObject.setHobbies(null);
        testObject.getUpperCaseHobbies();
    }

    @Test(timeout = 1000)
    public void testPerformance() {
        // 应该在 1 秒内完成
        testObject.getDescription();
    }

    @Ignore("This test is temporarily disabled")
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
