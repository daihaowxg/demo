package com.example.demo.mq.list;

import com.example.demo.mq.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wxg
 * @since 2025/4/11
 */
@RestController
public class ListQueueController {


    @Autowired
    private ListQueueService listQueueService;


    /**
     * 1. 生产消息
     */
    @PostMapping("/produce")
    public String produce(@RequestBody Task task) {
        listQueueService.produce(task);
        return "success";
    }

    /**
     * 3. 消费消息
     * 在调用此接口消费消息之前，可以在Redis中看到目前未消费的消息
     * 在调用此接口消费消息之后，Redis 中的消息消失
     * 消息队列中没有消息时，由于执行的是非阻塞操作，所以会直接返回 null
     */
    @GetMapping("/consume")
    public Task consume() {
        return listQueueService.consume();
    }

    @GetMapping("/consumeBlocked")
    public Task consumeBlocked() {
        return listQueueService.consumeBlocked();
    }
}
