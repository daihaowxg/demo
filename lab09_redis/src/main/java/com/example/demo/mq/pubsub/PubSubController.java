package com.example.demo.mq.pubsub;

import com.example.demo.mq.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wxg
 * @since 2025/4/11
 */
@RestController
public class PubSubController {

    @Autowired
    private TaskPublisher producer;


    @PostMapping("pubsub/produce")
    public void sendTask(@RequestBody Task task) {
        producer.publish(task);
    }
}
