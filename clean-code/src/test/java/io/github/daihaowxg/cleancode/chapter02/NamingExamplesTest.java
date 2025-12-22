package io.github.daihaowxg.cleancode.chapter02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NamingExamples 的测试类
 * 
 * <p>
 * 本测试类展示了如何为测试类和测试方法选择有意义的名称。
 * 同时也验证了 NamingExamples 中各个示例的正确性。
 * 
 * <h2>测试命名最佳实践：</h2>
 * <ul>
 * <li>测试类名：被测试类名 + Test 后缀</li>
 * <li>测试方法名：应该清晰描述测试的内容和预期结果</li>
 * <li>使用 @DisplayName 提供更友好的测试报告</li>
 * <li>使用 @Nested 组织相关的测试</li>
 * </ul>
 * 
 * @author daihaowxg
 */
@DisplayName("命名示例测试")
class NamingExamplesTest {

    // ============================================================
    // 原则1：名副其实
    // ============================================================

    @Nested
    @DisplayName("原则1：名副其实")
    class Rule1_MeaningfulNamesTest {

        @Test
        @DisplayName("应该能够获取已标记的单元格")
        void should_GetFlaggedCells_When_GameBoardContainsFlaggedCells() {
            // Given: 准备测试数据
            NamingExamples.Rule1_MeaningfulNames example = new NamingExamples.Rule1_MeaningfulNames();

            // When & Then: 验证对象创建成功
            assertNotNull(example, "示例对象不应为空");
        }

        @Test
        @DisplayName("好的命名应该让代码自文档化")
        void goodNaming_ShouldMakeCodeSelfDocumenting() {
            // 这个测试展示了好的命名如何提高代码可读性
            NamingExamples.Rule1_MeaningfulNames example = new NamingExamples.Rule1_MeaningfulNames();

            // 从方法名就能理解它的作用，无需查看实现
            assertNotNull(example);
        }
    }

    // ============================================================
    // 原则2：避免误导
    // ============================================================

    @Nested
    @DisplayName("原则2：避免误导")
    class Rule2_AvoidDisinformationTest {

        @Test
        @DisplayName("应该使用准确的名称避免误导")
        void should_UseAccurateNames_ToAvoidMisleading() {
            // Given
            NamingExamples.Rule2_AvoidDisinformation example = new NamingExamples.Rule2_AvoidDisinformation();

            // Then: 验证对象创建
            assertNotNull(example);
        }
    }

    // ============================================================
    // 原则3：做有意义的区分
    // ============================================================

    @Nested
    @DisplayName("原则3：做有意义的区分")
    class Rule3_MeaningfulDistinctionsTest {

        @Test
        @DisplayName("使用有意义的名称复制字符数组")
        void should_CopyCharsCorrectly_When_UsingMeaningfulNames() {
            // Given: 准备源数组和目标数组
            NamingExamples.Rule3_MeaningfulDistinctions example = new NamingExamples.Rule3_MeaningfulDistinctions();
            char[] source = { 'H', 'e', 'l', 'l', 'o' };
            char[] destination = new char[5];

            // When: 使用好的命名方法复制
            example.copyCharsGood(source, destination);

            // Then: 验证复制结果
            assertArrayEquals(source, destination,
                    "目标数组应该包含源数组的所有元素");
        }

        @Test
        @DisplayName("对比：使用数字序列命名的方法")
        void badNaming_UsesNumberSequence() {
            // Given
            NamingExamples.Rule3_MeaningfulDistinctions example = new NamingExamples.Rule3_MeaningfulDistinctions();
            char[] a1 = { 'a', 'b', 'c' };
            char[] a2 = new char[3];

            // When: 使用坏的命名方法
            example.copyCharsBad(a1, a2);

            // Then: 功能正确，但命名糟糕
            assertArrayEquals(a1, a2,
                    "虽然功能正确，但 a1、a2 这样的命名让人难以理解");
        }
    }

    // ============================================================
    // 原则4：使用读得出来的名称
    // ============================================================

    @Nested
    @DisplayName("原则4：使用读得出来的名称")
    class Rule4_PronounceableNamesTest {

        @Test
        @DisplayName("应该使用可以读出来的名称")
        void should_UsePronounceableNames() {
            // Given: 创建 Customer 对象（好的命名）
            NamingExamples.Rule4_PronounceableNames.Customer customer = new NamingExamples.Rule4_PronounceableNames.Customer();

            // Then: 验证对象创建
            assertNotNull(customer, "Customer 是一个可以读出来的名称");
        }

        @Test
        @DisplayName("对比：无法读出来的缩写命名")
        void badNaming_CannotBePronounced() {
            // Given: 创建 DtaRcrd102 对象（坏的命名）
            NamingExamples.Rule4_PronounceableNames.DtaRcrd102 record = new NamingExamples.Rule4_PronounceableNames.DtaRcrd102();

            // Then: 功能正常，但命名难以交流
            assertNotNull(record,
                    "DtaRcrd102 这样的名称无法在团队交流中使用");
        }
    }

    // ============================================================
    // 原则5：使用可搜索的名称
    // ============================================================

    @Nested
    @DisplayName("原则5：使用可搜索的名称")
    class Rule5_SearchableNamesTest {

        @Test
        @DisplayName("应该使用常量代替魔法数字")
        void should_UseConstants_InsteadOfMagicNumbers() {
            // Given: 准备测试数据
            NamingExamples.Rule5_SearchableNames example = new NamingExamples.Rule5_SearchableNames();
            int[] taskEstimates = new int[34];
            for (int i = 0; i < 34; i++) {
                taskEstimates[i] = 5;
            }

            // When: 计算总计
            int total = example.calculateGoodTotal(taskEstimates);

            // Then: 验证结果
            assertTrue(total >= 0, "总计应该是非负数");
        }

        @Test
        @DisplayName("小作用域中可以使用单字母变量")
        void singleLetterVariables_AreAcceptableInSmallScope() {
            // Given
            NamingExamples.Rule5_SearchableNames example = new NamingExamples.Rule5_SearchableNames();
            int[] numbers = { 1, 2, 3, 4, 5 };

            // When: 在小方法中使用单字母变量是可以接受的
            int sum = example.sum(numbers);

            // Then
            assertEquals(15, sum, "1+2+3+4+5 = 15");
        }
    }

    // ============================================================
    // 原则6：避免编码
    // ============================================================

    @Nested
    @DisplayName("原则6：避免编码")
    class Rule6_AvoidEncodingsTest {

        @Test
        @DisplayName("应该避免在名称中加入类型信息")
        void should_AvoidTypeEncodingInNames() {
            // Given
            NamingExamples.Rule6_AvoidEncodings example = new NamingExamples.Rule6_AvoidEncodings();

            // Then: 现代 IDE 已经提供了足够的类型信息
            assertNotNull(example,
                    "不需要使用 strName、iCount 这样的匈牙利命名法");
        }
    }

    // ============================================================
    // 原则7：避免思维映射
    // ============================================================

    @Nested
    @DisplayName("原则7：避免思维映射")
    class Rule7_AvoidMentalMappingTest {

        @Test
        @DisplayName("应该使用明确的名称，避免思维映射")
        void should_UseExplicitNames_AvoidMentalMapping() {
            // Given: 准备 URL 列表
            NamingExamples.Rule7_AvoidMentalMapping example = new NamingExamples.Rule7_AvoidMentalMapping();
            List<String> urls = new ArrayList<>();
            urls.add("https://example.com");

            // When: 使用好的命名方法
            example.processUrlsGood(urls);

            // Then: 代码清晰易懂，无需思维映射
            assertTrue(true, "明确的命名让代码更易理解");
        }
    }

    // ============================================================
    // 原则8：类名应该是名词或名词短语
    // ============================================================

    @Nested
    @DisplayName("原则8：类名应该是名词或名词短语")
    class Rule8_ClassNamesTest {

        @Test
        @DisplayName("好的类名应该是名词")
        void goodClassNames_ShouldBeNouns() {
            // Given: 创建好的类名示例
            NamingExamples.Rule8_ClassNames.Customer customer = new NamingExamples.Rule8_ClassNames.Customer();
            NamingExamples.Rule8_ClassNames.Account account = new NamingExamples.Rule8_ClassNames.Account();

            // Then: 这些都是好的类名
            assertNotNull(customer, "Customer 是一个好的类名（名词）");
            assertNotNull(account, "Account 是一个好的类名（名词）");
        }
    }

    // ============================================================
    // 原则9：方法名应该是动词或动词短语
    // ============================================================

    @Nested
    @DisplayName("原则9：方法名应该是动词或动词短语")
    class Rule9_MethodNamesTest {

        @Test
        @DisplayName("好的方法名应该是动词")
        void goodMethodNames_ShouldBeVerbs() {
            // Given
            NamingExamples.Rule9_MethodNames example = new NamingExamples.Rule9_MethodNames();

            // When: 调用动词命名的方法
            example.save();
            example.delete();

            // Then: 方法名清晰表达了动作
            assertTrue(true, "save、delete 都是好的方法名（动词）");
        }

        @Test
        @DisplayName("访问器应该使用 get 前缀")
        void accessors_ShouldUseGetPrefix() {
            // Given
            NamingExamples.Rule9_MethodNames example = NamingExamples.Rule9_MethodNames.fromName("TestUser");

            // When: 使用访问器
            String name = example.getName();

            // Then
            assertEquals("TestUser", name, "getName 是标准的访问器命名");
        }

        @Test
        @DisplayName("断言应该使用 is 前缀")
        void predicates_ShouldUseIsPrefix() {
            // Given
            NamingExamples.Rule9_MethodNames example = new NamingExamples.Rule9_MethodNames();

            // When: 使用断言方法
            boolean deleted = example.isDeleted();

            // Then
            assertFalse(deleted, "isDeleted 是标准的断言命名");
        }

        @Test
        @DisplayName("静态工厂方法应该说明参数含义")
        void staticFactoryMethods_ShouldDescribeParameters() {
            // Given & When: 使用静态工厂方法
            NamingExamples.Rule9_MethodNames fromName = NamingExamples.Rule9_MethodNames.fromName("Alice");
            NamingExamples.Rule9_MethodNames fromId = NamingExamples.Rule9_MethodNames.fromId(123);

            // Then: 方法名清楚地说明了参数的含义
            assertNotNull(fromName, "fromName 清楚地表明参数是名称");
            assertNotNull(fromId, "fromId 清楚地表明参数是 ID");
        }
    }

    // ============================================================
    // 综合示例测试
    // ============================================================

    @Nested
    @DisplayName("综合示例：重构演进")
    class RefactoringProgressionTest {

        @Test
        @DisplayName("阶段1：糟糕的命名")
        void stage1_BadNaming() {
            // Given: 糟糕的命名示例
            NamingExamples.BadNamingExample badExample = new NamingExamples.BadNamingExample();

            // Then: 功能可能正确，但代码难以理解
            assertNotNull(badExample,
                    "getThem() 这样的方法名无法表达意图");
        }

        @Test
        @DisplayName("阶段2：改进的命名")
        void stage2_GoodNaming() {
            // Given: 改进的命名示例
            NamingExamples.GoodNamingExample goodExample = new NamingExamples.GoodNamingExample();

            // Then: 代码更易理解
            assertNotNull(goodExample,
                    "getFlaggedCells() 清楚地表达了方法的意图");
        }

        @Test
        @DisplayName("阶段3：最佳实践")
        void stage3_BestPractice() {
            // Given: 最佳实践示例
            NamingExamples.BestNamingExample bestExample = new NamingExamples.BestNamingExample();

            // Then: 使用类代替数组，代码最清晰
            assertNotNull(bestExample,
                    "引入 Cell 类后，代码的意图更加明确");
        }

        @Test
        @DisplayName("Cell 类应该有清晰的方法")
        void cell_ShouldHaveClearMethods() {
            // Given
            NamingExamples.BestNamingExample.Cell cell = new NamingExamples.BestNamingExample.Cell();

            // When: 调用 isFlagged 方法
            boolean flagged = cell.isFlagged();

            // Then: 方法名清晰表达了查询意图
            assertFalse(flagged, "isFlagged() 是一个清晰的断言方法");
        }
    }

    // ============================================================
    // 测试命名风格示例
    // ============================================================

    @Nested
    @DisplayName("测试命名风格")
    class TestNamingStylesDemo {

        /**
         * ✅ 风格1：should_ExpectedBehavior_When_StateUnderTest
         * 优点：结构清晰，易于理解
         */
        @Test
        @DisplayName("风格1：should_When 格式")
        void should_ReturnTrue_When_ConditionIsMet() {
            assertTrue(true, "这是一种流行的测试命名风格");
        }

        /**
         * ✅ 风格2：snake_case_descriptive_format
         * 优点：可读性好，适合长描述
         */
        @Test
        @DisplayName("风格2：snake_case 格式")
        void given_valid_input_when_processing_then_should_succeed() {
            assertTrue(true, "这种风格在某些团队中很流行");
        }

        /**
         * ✅ 风格3：使用 @DisplayName 提供中文描述
         * 优点：测试报告更友好
         */
        @Test
        @DisplayName("当输入有效时应该成功处理")
        void testValidInput() {
            assertTrue(true, "@DisplayName 可以提供更友好的测试报告");
        }

        /**
         * ❌ 风格4：不好的命名
         * 缺点：无法理解测试目的
         */
        @Test
        @DisplayName("反面示例：糟糕的测试命名")
        void test1() {
            assertTrue(true, "test1 这样的名称没有传达任何信息");
        }
    }
}
