package io.github.daihaowxg.cleancode.chapter02;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 第2章：有意义的命名
 * 
 * <p>
 * 本包包含关于命名的代码示例，展示如何选择有意义的名称。
 * 每个核心原则都配有对比示例（❌ 不好 vs ✅ 好）。
 * 
 * <h2>核心原则：</h2>
 * <ul>
 * <li>名副其实：名称应该告诉你它为什么存在、做什么事、如何使用</li>
 * <li>避免误导：避免使用容易引起误解的名称</li>
 * <li>做有意义的区分：不要使用 a1, a2 这样的名称</li>
 * <li>使用读得出来的名称：避免使用缩写和自造词</li>
 * <li>使用可搜索的名称：单字母名称和数字常量很难搜索</li>
 * <li>避免编码：不要在名称中加入类型信息</li>
 * <li>避免思维映射：不应当让读者在脑中把你的名称翻译为他们熟知的名称</li>
 * <li>类名应该是名词或名词短语</li>
 * <li>方法名应该是动词或动词短语</li>
 * <li>别抖机灵：不要使用俏皮话、俚语、文化相关的笑话</li>
 * <li>每个概念对应一个词：给每个抽象概念选一个词，并且一以贯之</li>
 * <li>别用双关语：避免将同一个词用于不同的目的</li>
 * <li>使用解决方案领域名称：使用计算机科学术语、算法名、模式名等</li>
 * <li>使用源自所涉问题领域的名称：使用业务领域的术语</li>
 * <li>添加有意义的语境：通过类、函数、命名空间等添加语境</li>
 * <li>不要添加没用的语境：只要短名称足够清楚，就不要添加多余的语境</li>
 * </ul>
 * 
 * @author daihaowxg
 */
@SuppressWarnings("unused") // 示例代码，字段用于展示命名规范而非实际使用
public class NamingExamples {

    private NamingExamples() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // ============================================================
    // 原则1：名副其实 - 名称应该告诉你它为什么存在、做什么事、如何使用
    // ============================================================

    /**
     * 原则1：名副其实
     */
    public static class Rule1_MeaningfulNames {

        // ❌ 不好：变量名没有传达任何信息
        private int d; // 经过的时间，以天为单位

        // ✅ 好：名称清晰地表达了意图
        private int elapsedTimeInDays;
        private int daysSinceCreation;
        private int daysSinceModification;

        // ❌ 不好：需要注释才能理解
        public List<int[]> getThem(List<int[]> theList) {
            List<int[]> list1 = new ArrayList<>();
            for (int[] x : theList) {
                if (x[0] == 4) {
                    list1.add(x);
                }
            }
            return list1;
        }

        // ✅ 好：名称本身就说明了一切
        public List<int[]> getFlaggedCells(List<int[]> gameBoard) {
            List<int[]> flaggedCells = new ArrayList<>();
            final int STATUS_VALUE = 0;
            final int FLAGGED = 4;

            for (int[] cell : gameBoard) {
                if (cell[STATUS_VALUE] == FLAGGED) {
                    flaggedCells.add(cell);
                }
            }
            return flaggedCells;
        }
    }

    // ============================================================
    // 原则2：避免误导 - 避免使用容易引起误解的名称
    // ============================================================

    /**
     * 原则2：避免误导
     */
    public static class Rule2_AvoidDisinformation {

        // ❌ 不好：accountList 不是真正的 List，容易误导
        private String accountList; // 实际上是逗号分隔的字符串

        // ✅ 好：使用更准确的名称
        private String accountGroup;
        private String accountsString;

        // ❌ 不好：使用了容易混淆的名称
        private String XYZControllerForEfficientHandlingOfStrings;
        private String XYZControllerForEfficientStorageOfStrings;
        // 这两个名称太相似，难以区分

        // ✅ 好：使用清晰、易区分的名称
        private String stringProcessor;
        private String stringRepository;

        // ❌ 不好：小写字母 l 和数字 1，大写字母 O 和数字 0 容易混淆
        // int a = l; // l 看起来像 1
        // if (O == l) { ... } // O 看起来像 0，l 看起来像 1

        // ✅ 好：避免使用容易混淆的字符
        // int amount = length;
        // if (zero == one) { ... }
    }

    // ============================================================
    // 原则3：做有意义的区分 - 不要使用 a1, a2 这样的名称
    // ============================================================

    /**
     * 原则3：做有意义的区分
     */
    public static class Rule3_MeaningfulDistinctions {

        // ❌ 不好：使用数字序列命名
        public void copyCharsBad(char[] a1, char[] a2) {
            for (int i = 0; i < a1.length; i++) {
                a2[i] = a1[i];
            }
        }

        // ✅ 好：使用有意义的名称
        public void copyCharsGood(char[] source, char[] destination) {
            for (int i = 0; i < source.length; i++) {
                destination[i] = source[i];
            }
        }

        // ❌ 不好：废话式命名，无法区分
        public class BadProduct {
        }

        public class BadProductInfo {
        }

        public class BadProductData {
        }
        // Info 和 Data 就像 a, an, the 一样，是意义含混的废话

        // ✅ 好：明确区分不同的概念
        public class GoodProduct {
        } // 产品实体

        public class ProductSpecification {
        } // 产品规格

        public class ProductInventory {
        } // 产品库存
    }

    // ============================================================
    // 原则4：使用读得出来的名称 - 避免使用缩写和自造词
    // ============================================================

    /**
     * 原则4：使用读得出来的名称
     */
    public static class Rule4_PronounceableNames {

        // ❌ 不好：无法读出来的缩写
        public static class DtaRcrd102 {
            private String pszqint = "102";
            private Date genymdhms; // generation date, year, month, day, hour, minute, second
            private Date modymdhms; // modification date, year, month, day, hour, minute, second
        }

        // ✅ 好：可以读出来的完整名称
        public static class Customer {
            private String recordId = "102";
            private Date generationTimestamp;
            private Date modificationTimestamp;
        }
    }

    // ============================================================
    // 原则5：使用可搜索的名称 - 单字母名称和数字常量很难搜索
    // ============================================================

    /**
     * 原则5：使用可搜索的名称
     */
    public static class Rule5_SearchableNames {

        // ❌ 不好：使用魔法数字和单字母变量
        public int calculateBadTotal(int[] t) {
            int s = 0;
            for (int j = 0; j < 34; j++) {
                s += (t[j] * 4) / 5;
            }
            return s;
        }

        // ✅ 好：使用可搜索的名称和常量
        private static final int NUMBER_OF_TASKS = 34;
        private static final int WORK_DAYS_PER_WEEK = 5;

        public int calculateGoodTotal(int[] taskEstimates) {
            int totalWorkDays = 0;
            for (int taskIndex = 0; taskIndex < NUMBER_OF_TASKS; taskIndex++) {
                int estimatedDays = taskEstimates[taskIndex];
                int realWorkDays = estimatedDays / WORK_DAYS_PER_WEEK;
                totalWorkDays += realWorkDays;
            }
            return totalWorkDays;
        }

        // 注意：单字母名称仅适用于短方法中的本地变量
        // ✅ 可以接受：在小作用域中使用单字母
        public int sum(int[] numbers) {
            int s = 0;
            for (int n : numbers) {
                s += n;
            }
            return s;
        }
    }

    // ============================================================
    // 原则6：避免编码 - 不要在名称中加入类型信息
    // ============================================================

    /**
     * 原则6：避免编码（匈牙利命名法、成员前缀等）
     */
    public static class Rule6_AvoidEncodings {

        // ❌ 不好：使用匈牙利命名法（类型编码）
        private String strName;
        private int iCount;
        private boolean bFlag;

        // ❌ 不好：使用成员前缀
        private String m_description;
        private int m_age;

        // ✅ 好：不需要类型编码和前缀
        private String name;
        private int count;
        private boolean isActive;
        private String description;
        private int age;

        // ❌ 不好：接口和实现的命名
        interface IShapeFactory {
        }

        class BadShapeFactoryImpl implements IShapeFactory {
        }

        // ✅ 好：接口用普通名称，实现类加后缀
        interface ShapeFactory {
        }

        class GoodShapeFactoryImpl implements ShapeFactory {
        }

        // 或者
        class CircleFactory implements ShapeFactory {
        }
    }

    // ============================================================
    // 原则7：避免思维映射 - 不应当让读者在脑中把你的名称翻译为他们熟知的名称
    // ============================================================

    /**
     * 原则7：避免思维映射
     */
    public static class Rule7_AvoidMentalMapping {

        // ❌ 不好：使用单字母变量名（除了循环计数器）
        public void processUrls(List<String> urls) {
            for (String u : urls) {
                String r = getResponse(u); // r 是什么？需要思维映射
                // ... 处理 r
            }
        }

        // ✅ 好：使用明确的名称
        public void processUrlsGood(List<String> urls) {
            for (String url : urls) {
                String response = getResponse(url);
                // ... 处理 response
            }
        }

        private String getResponse(String url) {
            return "response";
        }
    }

    // ============================================================
    // 原则8：类名应该是名词或名词短语
    // ============================================================

    /**
     * 原则8：类名应该是名词或名词短语
     */
    public static class Rule8_ClassNames {

        // ✅ 好的类名：名词或名词短语
        public static class Customer {
        }

        public static class WikiPage {
        }

        public static class Account {
        }

        public static class AddressParser {
        }

        // ❌ 不好的类名：动词
        // class Process {} // 太模糊
        // class Manager {} // 太模糊
        // class Data {} // 太模糊
        // class Info {} // 太模糊

        // ✅ 更好的替代
        public static class OrderProcessor {
        }

        public static class AccountManager {
        }

        public static class CustomerData {
        }

        public static class ProductInformation {
        }
    }

    // ============================================================
    // 原则9：方法名应该是动词或动词短语
    // ============================================================

    /**
     * 原则9：方法名应该是动词或动词短语
     */
    public static class Rule9_MethodNames {

        private String name;
        private boolean deleted;

        // ✅ 好的方法名：动词或动词短语
        public void save() {
        }

        public void delete() {
        }

        public void processPayment() {
        }

        public void sendEmail() {
        }

        // ✅ 访问器、修改器、断言使用 get、set、is 前缀
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isDeleted() {
            return deleted;
        }

        // ✅ 重载构造器时，使用静态工厂方法，名称说明参数
        public static Rule9_MethodNames fromName(String name) {
            Rule9_MethodNames instance = new Rule9_MethodNames();
            instance.name = name;
            return instance;
        }

        public static Rule9_MethodNames fromId(int id) {
            Rule9_MethodNames instance = new Rule9_MethodNames();
            // ... 根据 id 初始化
            return instance;
        }

        // ❌ 不好：直接使用构造器，参数含义不明确
        // new Rule9_MethodNames("John"); // 这是什么？名称？ID？

        // ✅ 好：使用静态工厂方法，意图明确
        // Rule9_MethodNames.fromName("John");
    }

    // ============================================================
    // 综合示例：重构前后对比
    // ============================================================

    /**
     * 综合示例：重构前 - 糟糕的命名
     */
    public static class BadNamingExample {
        private List<int[]> theList;

        public List<int[]> getThem() {
            List<int[]> list1 = new ArrayList<>();
            for (int[] x : theList) {
                if (x[0] == 4) {
                    list1.add(x);
                }
            }
            return list1;
        }
    }

    /**
     * 综合示例：重构后 - 良好的命名
     */
    public static class GoodNamingExample {
        private static final int STATUS_VALUE = 0;
        private static final int FLAGGED = 4;

        private List<int[]> gameBoard;

        public List<int[]> getFlaggedCells() {
            List<int[]> flaggedCells = new ArrayList<>();
            for (int[] cell : gameBoard) {
                if (isFlagged(cell)) {
                    flaggedCells.add(cell);
                }
            }
            return flaggedCells;
        }

        private boolean isFlagged(int[] cell) {
            return cell[STATUS_VALUE] == FLAGGED;
        }
    }

    /**
     * 综合示例：进一步改进 - 使用类代替数组
     */
    public static class BestNamingExample {
        private List<Cell> gameBoard;

        public List<Cell> getFlaggedCells() {
            List<Cell> flaggedCells = new ArrayList<>();
            for (Cell cell : gameBoard) {
                if (cell.isFlagged()) {
                    flaggedCells.add(cell);
                }
            }
            return flaggedCells;
        }

        // 使用类代替简单的 int 数组，使代码更加清晰
        public static class Cell {
            private int status;
            private static final int FLAGGED = 4;

            public boolean isFlagged() {
                return status == FLAGGED;
            }
        }
    }

    // ============================================================
    // 原则10：别抖机灵
    // ============================================================

    /**
     * 原则10：别抖机灵
     * <p>
     * 不要使用俏皮话、俚语、文化相关的笑话作为名称
     */
    public static class Rule10_DontBeCute {

        // ❌ 不好：使用俏皮话、双关语
        public void holyHandGrenade() {
            // 这是 Monty Python 的梗，不是所有人都知道
        }

        public void whack() {
            // whack 是俚语，意思不明确
        }

        public void eatMyShorts() {
            // 这是《辛普森一家》的台词，不专业
        }

        // ✅ 好：使用清晰、专业的名称
        public void deleteItems() {
            // 清楚地表达了意图
        }

        public void kill() {
            // 在编程领域，kill 是标准术语
        }

        public void abort() {
            // abort 是标准的技术术语
        }
    }

    // ============================================================
    // 原则11：每个概念对应一个词
    // ============================================================

    /**
     * 原则11：每个概念对应一个词
     * <p>
     * 给每个抽象概念选一个词，并且一以贯之
     */
    public static class Rule11_PickOneWordPerConcept {

        // ❌ 不好：同一概念使用不同的词
        public static class BadUserController {
            public void fetchUser() {
            } // 使用 fetch
        }

        public static class BadOrderController {
            public void retrieveOrder() {
            } // 使用 retrieve
        }

        public static class BadProductController {
            public void getProduct() {
            } // 使用 get
        }
        // fetch、retrieve、get 都表示"获取"，应该统一

        // ❌ 不好：混用 controller、manager、driver
        public static class InconsistentUserController {
        }

        public static class InconsistentOrderManager {
        }

        public static class InconsistentProductDriver {
        }
        // 这三个类做类似的事情，应该使用统一的后缀

        // ✅ 好：每个概念使用同一个词
        public static class GoodUserController {
            public void getUser() {
            }
        }

        public static class GoodOrderController {
            public void getOrder() {
            }
        }

        public static class GoodProductController {
            public void getProduct() {
            }
        }
        // 统一使用 get 表示获取，使用 Controller 作为后缀
    }

    // ============================================================
    // 原则12：别用双关语
    // ============================================================

    /**
     * 原则12：别用双关语
     * <p>
     * 避免将同一个词用于不同的目的
     */
    public static class Rule12_AvoidPuns {

        private List<Integer> numbers = new ArrayList<>();

        // ❌ 不好：add 有双关含义
        // 在不同的类中，add 可能表示不同的意思：
        // - 在 Calculator 类中，add 表示"数学加法"
        // - 在 List 类中，add 表示"将元素添加到集合"
        // 如果你的新方法是把两个现有值相加，就不应该叫 add
        // 因为这与其他 add 方法的语义不同
        public int add(int a, int b) {
            return a + b; // 数学加法
        }

        public void add(int number) {
            numbers.add(number); // 添加到集合
        }
        // 同一个类中，add 有两种不同的含义

        // ✅ 好：使用不同的词表示不同的概念
        public int sum(int a, int b) {
            return a + b; // 数学加法用 sum
        }

        public void append(int number) {
            numbers.add(number); // 添加到集合用 append
        }

        // 或者
        public int calculate(int a, int b) {
            return a + b;
        }

        public void insert(int number) {
            numbers.add(number);
        }
    }

    // ============================================================
    // 原则13：使用解决方案领域名称
    // ============================================================

    /**
     * 原则13：使用解决方案领域名称
     * <p>
     * 使用计算机科学术语、算法名、模式名、数学术语等
     */
    public static class Rule13_UseSolutionDomainNames {

        // ✅ 好：使用设计模式名称
        public interface AccountVisitor {
            // Visitor 是设计模式名称，程序员都知道
        }

        public class JobQueue {
            // Queue 是数据结构名称
        }

        public class AccountFactory {
            // Factory 是设计模式名称
        }

        // ✅ 好：使用算法名称
        public void quickSort(int[] array) {
            // quickSort 是众所周知的算法
        }

        public void binarySearch(int[] array, int target) {
            // binarySearch 是标准算法名称
        }

        // ✅ 好：使用计算机科学术语
        public class TreeNode {
            // Tree 是数据结构术语
        }

        public class Adapter {
            // Adapter 是设计模式
        }

        public class Decorator {
            // Decorator 是设计模式
        }
    }

    // ============================================================
    // 原则14：使用源自所涉问题领域的名称
    // ============================================================

    /**
     * 原则14：使用源自所涉问题领域的名称
     * <p>
     * 如果没有合适的技术术语，就使用业务领域的名称
     */
    public static class Rule14_UseProblemDomainNames {

        // ✅ 好：使用业务领域术语（银行系统）
        public class Account {
            private String accountNumber;
            private double balance;
            private String accountHolder;

            public void deposit(double amount) {
            }

            public void withdraw(double amount) {
            }

            public void transfer(Account target, double amount) {
            }
        }

        // ✅ 好：使用业务领域术语（电商系统）
        public class ShoppingCart {
            private List<CartItem> items;

            public void addItem(CartItem item) {
            }

            public void removeItem(CartItem item) {
            }

            public double calculateTotal() {
                return 0.0;
            }

            public void checkout() {
            }
        }

        public class CartItem {
            private String productId;
            private int quantity;
            private double unitPrice;
        }

        // ✅ 好：使用业务领域术语（医疗系统）
        public class Patient {
            private String patientId;
            private String diagnosis;
            private List<Prescription> prescriptions;
        }

        public class Prescription {
            private String medication;
            private String dosage;
            private int duration;
        }
    }

    // ============================================================
    // 原则15：添加有意义的语境
    // ============================================================

    /**
     * 原则15：添加有意义的语境
     * <p>
     * 通过类、函数、命名空间等添加语境，让名称更清晰
     */
    public static class Rule15_AddMeaningfulContext {

        // ❌ 不好：缺乏语境
        public class BadExample {
            private String street;
            private String city;
            private String state;
            private String zipCode;

            // 这些变量是什么的地址？用户？公司？配送地址？
        }

        // ✅ 好：通过类名添加语境
        public class Address {
            private String street;
            private String city;
            private String state;
            private String zipCode;

            // 现在很清楚这些是地址的组成部分
        }

        // ✅ 好：通过前缀添加语境（当无法使用类时）
        public class GoodExample {
            private String addressStreet;
            private String addressCity;
            private String addressState;
            private String addressZipCode;
        }

        // ✅ 更好：使用专门的类
        public class Customer {
            private String name;
            private Address shippingAddress; // 配送地址
            private Address billingAddress; // 账单地址
        }
    }

    // ============================================================
    // 原则16：不要添加没用的语境
    // ============================================================

    /**
     * 原则16：不要添加没用的语境
     * <p>
     * 只要短名称足够清楚，就不要添加多余的语境
     */
    public static class Rule16_DontAddGratuitousContext {

        // ❌ 不好：添加了没用的语境
        // 假设我们在开发一个名为 "Gas Station Deluxe" 的应用
        public class GSDAccountAddress {
            // GSD 前缀是多余的
        }

        public class GSDAccountCustomer {
            // 每个类都加 GSD 前缀，毫无必要
        }

        // ✅ 好：只在必要时添加语境
        public class AccountAddress {
            // 简洁明了
        }

        public class Customer {
            // 不需要前缀
        }

        // ❌ 不好：过度限定
        public class MailingAddress {
            private String mailingAddressStreet;
            private String mailingAddressCity;
            private String mailingAddressState;
            // mailingAddress 前缀是多余的，因为已经在 MailingAddress 类中了
        }

        // ✅ 好：适度的语境
        public class MailingAddressGood {
            private String street;
            private String city;
            private String state;
            // 类名已经提供了足够的语境
        }

        // ✅ 好：只在需要区分时添加语境
        public static class CustomerAccount {
            private String mailingAddress; // 需要区分不同类型的地址
            private String billingAddress;
            private String shippingAddress;
        }
    }
}
