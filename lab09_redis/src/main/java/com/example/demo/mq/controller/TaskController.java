package com.example.demo.mq.controller;

import com.example.demo.mq.Task;
import com.example.demo.mq.TaskProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wxg
 * @since 2025/4/11
 */
@RestController
public class TaskController {

    @Autowired
    private TaskProducer taskProducer;

    @PostMapping("/tasks")
    public String createTask(@RequestBody Task task) {
        taskProducer.sendTask(task);
        return "Task sent to queue: " + task.getTaskId();
    }
}
