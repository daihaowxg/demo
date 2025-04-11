package com.example.demo.mq;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wxg
 * @since 2025/4/11
 */
@Data
public class Task implements Serializable {
    private String taskId;
    private String description;
}
