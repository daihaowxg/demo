package io.github.daihaowxg.druid;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 验证 removeAbandonedTimeout 的单位
 * 
 * <p>测试目标：确认 removeAbandonedTimeout 的单位是秒还是毫秒</p>
 * 
 * <p>测试方法：</p>
 * <ul>
 *   <li>1. 获取 DruidDataSource 实例</li>
 *   <li>2. 读取配置的 removeAbandonedTimeout 值</li>
 *   <li>3. 创建一个不关闭的连接</li>
 *   <li>4. 等待指定时间后观察连接是否被回收</li>
 * </ul>
 *
 * @author daihaowxg
 */
@SpringBootTest
public class RemoveAbandonedTimeoutUnitTest {
    
    @Autowired
    private DataSource dataSource;
    
    /**
     * 测试 1：验证配置值的单位
     * 
     * <p>通过查看 DruidDataSource 的源码和配置值来确认单位</p>
     */
    @Test
    void testRemoveAbandonedTimeoutUnit() {
        if (dataSource instanceof DruidDataSource druidDataSource) {
            // 获取配置的超时时间
            long timeout = druidDataSource.getRemoveAbandonedTimeoutMillis();
            
            System.out.println("\n=== removeAbandonedTimeout 单位验证 ===");
            System.out.println("配置文件中设置的值: 180 (application.yml 中的 remove-abandoned-timeout)");
            System.out.println("DruidDataSource.getRemoveAbandonedTimeoutMillis() 返回值: " + timeout + " 毫秒");
            System.out.println("转换为秒: " + (timeout / 1000) + " 秒");
            
            // 验证：如果配置的是 180，返回值应该是 180000 毫秒（180秒）
            if (timeout == 180000) {
                System.out.println("\n✅ 结论：配置文件中的单位是【秒】");
                System.out.println("   - 配置值 180 被转换为 180000 毫秒");
                System.out.println("   - 即 180 秒 = 3 分钟");
            } else if (timeout == 180) {
                System.out.println("\n❌ 结论：配置文件中的单位是【毫秒】");
                System.out.println("   - 配置值 180 直接作为 180 毫秒使用");
            } else {
                System.out.println("\n⚠️  警告：返回值与预期不符，请检查配置");
            }
            
            // 打印其他相关配置
            System.out.println("\n=== 其他时间相关配置（用于对比）===");
            System.out.println("maxWait (最大等待时间): " + druidDataSource.getMaxWait() + " 毫秒");
            System.out.println("timeBetweenEvictionRunsMillis: " + druidDataSource.getTimeBetweenEvictionRunsMillis() + " 毫秒");
            System.out.println("minEvictableIdleTimeMillis: " + druidDataSource.getMinEvictableIdleTimeMillis() + " 毫秒");
            
            System.out.println("\n📝 注意：");
            System.out.println("   - maxWait 等配置在 application.yml 中也是毫秒单位");
            System.out.println("   - 但 removeAbandonedTimeout 在配置文件中是秒单位");
            System.out.println("   - Druid 内部会自动转换为毫秒");
        }
    }
    
    /**
     * 测试 2：实际验证连接回收时间
     * 
     * <p>注意：这个测试需要等待较长时间，默认注释掉</p>
     * <p>如果要运行此测试，请：</p>
     * <ul>
     *   <li>1. 将 application.yml 中的 remove-abandoned-timeout 改为 5（5秒）</li>
     *   <li>2. 取消下面的 @Disabled 注解</li>
     *   <li>3. 运行测试并观察日志</li>
     * </ul>
     * 
     * <p><strong>重要提示</strong>：removeAbandoned 不是主动定期检查，而是在以下情况触发：</p>
     * <ul>
     *   <li>1. 获取新连接时（getConnection）</li>
     *   <li>2. 连接池维护线程运行时（需要配置 timeBetweenEvictionRunsMillis）</li>
     * </ul>
     */
    @Test
    @Disabled("需要等待较长时间，手动启用此测试")
    void testActualRemoveAbandonedTimeout() throws SQLException, InterruptedException {
        if (!(dataSource instanceof DruidDataSource druidDataSource)) {
            System.out.println("数据源不是 DruidDataSource，跳过测试");
            return;
        }
        
        long timeout = druidDataSource.getRemoveAbandonedTimeoutMillis();
        long timeoutSeconds = timeout / 1000;
        
        System.out.println("\n=== 实际连接回收时间验证 ===");
        System.out.println("配置的超时时间: " + timeoutSeconds + " 秒 (" + timeout + " 毫秒)");
        System.out.println("当前活跃连接数: " + druidDataSource.getActiveCount());
        
        // 获取一个连接但不关闭（模拟连接泄漏）
        System.out.println("\n1. 获取一个连接（不关闭，模拟连接泄漏）...");
        Connection leakedConn = dataSource.getConnection();
        System.out.println("   连接已获取，当前活跃连接数: " + druidDataSource.getActiveCount());
        
        // 如果超时时间太长，只等待一部分时间
        long waitTime = Math.min(timeoutSeconds + 5, 30); // 最多等待30秒
        
        System.out.println("\n2. 等待 " + waitTime + " 秒...");
        System.out.println("   （如果单位是秒，连接应该在 " + timeoutSeconds + " 秒后被回收）");
        System.out.println("   （如果单位是毫秒，连接应该立即被回收）");
        System.out.println("   ⚠️  注意：需要通过获取新连接来触发 removeAbandoned 检查");
        
        boolean connectionRemoved = false;
        
        for (int i = 1; i <= waitTime; i++) {
            Thread.sleep(1000);
            
            // 关键：通过尝试获取新连接来触发 removeAbandoned 检查
            if (i > timeoutSeconds) {
                try {
                    System.out.println("   " + i + " 秒 - 尝试获取新连接以触发检查...");
                    Connection triggerConn = dataSource.getConnection();
                    System.out.println("   " + i + " 秒 - 获取连接后活跃数: " + druidDataSource.getActiveCount());
                    
                    // 关闭触发连接
                    triggerConn.close();
                    
                    // 在关闭后检查活跃连接数
                    int activeCountAfterClose = druidDataSource.getActiveCount();
                    System.out.println("   " + i + " 秒 - 关闭连接后活跃数: " + activeCountAfterClose);
                    
                    // 检查泄漏的连接是否已被回收
                    // 如果只剩 0 个连接，说明 leakedConn 已被回收
                    if (activeCountAfterClose == 0) {
                        System.out.println("\n✅ 连接在约 " + i + " 秒后被回收");
                        System.out.println("   结论：单位是【秒】");
                        System.out.println("   验证成功：配置的 " + timeoutSeconds + " 秒超时生效");
                        connectionRemoved = true;
                        break;
                    }
                } catch (SQLException e) {
                    System.out.println("   获取连接失败: " + e.getMessage());
                }
            } else {
                int activeCount = druidDataSource.getActiveCount();
                System.out.println("   " + i + " 秒 - 活跃连接数: " + activeCount);
            }
        }
        
        System.out.println("\n3. 最终活跃连接数: " + druidDataSource.getActiveCount());
        
        if (connectionRemoved) {
            System.out.println("\n💡 提示：查看上面的日志，应该能看到 'abandon connection' 的 WARN 日志");
            System.out.println("   日志中会显示连接是在哪里被获取的（堆栈信息）");
        } else {
            System.out.println("\n⚠️  警告：连接未被回收，可能的原因：");
            System.out.println("   1. 超时时间设置太长");
            System.out.println("   2. removeAbandoned 未正确配置");
            System.out.println("   3. 需要更多时间等待");
        }
        
        // 清理：关闭泄漏的连接（如果还存在）
        try {
            if (leakedConn != null && !leakedConn.isClosed()) {
                leakedConn.close();
            }
        } catch (SQLException e) {
            // 连接可能已被 Druid 强制回收
            System.out.println("\n✅ 连接已被 Druid 强制回收（关闭时抛出异常）");
        }
    }
    
    /**
     * 测试 3：查看 Druid 配置摘要
     */
    @Test
    void testDruidConfigurationSummary() {
        if (dataSource instanceof DruidDataSource druidDataSource) {
            System.out.println("\n=== Druid 时间相关配置摘要 ===\n");
            
            System.out.println("配置项                              | 配置文件单位 | 内部存储单位 | 实际值");
            System.out.println("-----------------------------------|----------|----------|----------");
            System.out.printf("maxWait                            | 毫秒      | 毫秒      | %d ms%n", 
                druidDataSource.getMaxWait());
            System.out.printf("timeBetweenEvictionRunsMillis      | 毫秒      | 毫秒      | %d ms%n", 
                druidDataSource.getTimeBetweenEvictionRunsMillis());
            System.out.printf("minEvictableIdleTimeMillis         | 毫秒      | 毫秒      | %d ms%n", 
                druidDataSource.getMinEvictableIdleTimeMillis());
            System.out.printf("removeAbandonedTimeout             | 秒       | 毫秒      | %d ms (%d s)%n", 
                druidDataSource.getRemoveAbandonedTimeoutMillis(),
                druidDataSource.getRemoveAbandonedTimeoutMillis() / 1000);
            
            System.out.println("\n📌 关键结论：");
            System.out.println("   - 大部分时间配置在 application.yml 中使用【毫秒】");
            System.out.println("   - removeAbandonedTimeout 在 application.yml 中使用【秒】");
            System.out.println("   - Druid 内部统一使用毫秒存储");
        }
    }
}
