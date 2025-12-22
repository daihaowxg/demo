package io.github.daihaowxg.mybatis.mapper.secondary;

import io.github.daihaowxg.mybatis.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

/**
 * 第二个数据源的 User Mapper
 * 
 * <p>
 * 使用注解方式定义 SQL，也可以使用 XML 方式
 * </p>
 * 
 * <p>
 * <b>关键点：</b>
 * </p>
 * <ul>
 * <li>此 Mapper 位于 io.github.daihaowxg.mybatis.mapper.secondary 包下</li>
 * <li>会被 SecondaryMyBatisConfig 的 @MapperScan 扫描到</li>
 * <li>使用第二个数据源（secondaryDataSource）</li>
 * </ul>
 *
 * @author daihaowxg
 */
@Mapper
public interface SecondaryUserMapper {

    /**
     * 查询所有用户
     */
    @Select("SELECT * FROM users")
    @Results(id = "userResultMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "email", column = "email")
    })
    List<User> findAll();

    /**
     * 根据 ID 查询用户
     */
    @Select("SELECT * FROM users WHERE id = #{id}")
    @ResultMap("userResultMap")
    Optional<User> findById(@Param("id") Long id);

    /**
     * 根据名称查询用户
     */
    @Select("SELECT * FROM users WHERE name LIKE CONCAT('%', #{name}, '%')")
    @ResultMap("userResultMap")
    List<User> findByName(@Param("name") String name);

    /**
     * 插入用户
     */
    @Insert("INSERT INTO users (name, email) VALUES (#{name}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    /**
     * 更新用户
     */
    @Update("UPDATE users SET name = #{name}, email = #{email} WHERE id = #{id}")
    int update(User user);

    /**
     * 删除用户
     */
    @Delete("DELETE FROM users WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    /**
     * 统计用户数量
     */
    @Select("SELECT COUNT(*) FROM users")
    long count();
}
