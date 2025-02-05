package com.crhms.cdmp.ds.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author wxg
 * @since 2025/2/5
 */
@TableName("sys_user")
@Data
public class User {

    private Long id;
    private String username;
    private String password;
}
