package io.github.daihaowxg.spring.jdbc.mapper;

import io.github.daihaowxg.spring.jdbc.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis Mapper 接口。
 * <p>
 * 这是 MyBatis 的核心。与 {@link io.github.daihaowxg.spring.jdbc.repository.StandardJdbcRepository} 和
 * {@link io.github.daihaowxg.spring.jdbc.repository.SpringJdbcRepository} 相比：
 * <ul>
 *   <li>这是一个纯接口，不需要实现类。MyBatis 会自动为我们生成代理实现。</li>
 *   <li>SQL 语句被移到了 XML 文件中 (UserMapper.xml)，实现了代码与 SQL 的解耦。</li>
 *   <li>方法签名非常干净，不需要处理 JDBC 的 Connection, PreparedStatement, ResultSet。</li>
 *   <li>Mapper 的方法名与 XML 中的 id 对应。</li>
 * </ul>
 */
@Mapper
public interface UserMapper {

    /**
     * 保存用户。
     * 对应的 XML 配置使用了 useGeneratedKeys="true" 来自动回填主键到 User 对象。
     */
    void save(User user);

    /**
     * 根据 ID 查询用户。
     * XML 中的 resultMap 负责将数据库列映射到 User 对象属性。
     */
    Optional<User> findById(@Param("id") Long id);

    /**
     * 查询所有用户。
     */
    List<User> findAll();

    /**
     * 更新用户。
     */
    void update(User user);

    /**
     * 根据 ID 删除用户。
     */
    void deleteById(@Param("id") Long id);
}
