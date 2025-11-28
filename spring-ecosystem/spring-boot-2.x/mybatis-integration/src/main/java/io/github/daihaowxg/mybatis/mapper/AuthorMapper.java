package io.github.daihaowxg.mybatis.mapper;

import io.github.daihaowxg.mybatis.model.Author;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthorMapper {
    /**
     * 动态更新 Author
     */
    int updateAuthorIfNecessary(Author author);

    /**
     * 用于测试准备数据
     */
    void insertAuthor(Author author);

    /**
     * 用于验证数据
     */
    Author findAuthorById(@Param("id") int id);
}