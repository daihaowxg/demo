package com.example.hutool;

import cn.hutool.core.util.IdUtil;
import org.junit.jupiter.api.Test;

class IdUtilTest {

    @Test
    void testIdUtil() {
        // Test code for IdUtil can be added here
        System.out.println(IdUtil.getSnowflakeNextId());
        System.out.println(IdUtil.objectId());
        IdUtil.getSnowflake();
    }
}
