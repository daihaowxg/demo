package com.example.hutool;

import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;

public class DateUtilTest {

    @Test
    void testDateUtil() {
        // Test code for DateUtil can be added here
        System.out.println(DateUtil.format(DateUtil.parse("20221111"), "yyyy-MM-dd"));
    }
}
