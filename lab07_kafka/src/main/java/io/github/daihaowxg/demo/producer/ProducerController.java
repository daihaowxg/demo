package io.github.daihaowxg.demo.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/producer")
public class ProducerController {

    @Resource
    private KafkaTemplate<String, String> kafka;

    @PostMapping
    public String data(@RequestBody String msg) {
        // 通过Kafka发出数据
        String topic = "test";
        kafka.send(topic, msg);
        return "ok";
    }
}
