package com.example.demo;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.net.InetAddress;


/**
 * 1. 启动之前需要先将 Kafka Server 启动
 * 2. 启动 KafkaApplication
 * 3. ProducerController 接口传参，内部通过 KafkaTemplate 的 send 方法给指定 Topic 发送数据
 * 4. KafkaConsumer 接收到事件，在控制台打印出收到的信息
 */
@SpringBootApplication
public class KafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaApplication.class, args);
    }


    @Bean
    ApplicationRunner applicationRunner(Environment environment) {
        return args -> {
            String appName = environment.getProperty("spring.application.name") != null ? environment.getProperty("spring.application.name") : "";
            String port = environment.getProperty("server.port") != null ? environment.getProperty("server.port") : "8080";
            String path = environment.getProperty("server.servlet.context-path") != null ? environment.getProperty("server.servlet.context-path") : "";
            String ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println(
                    "\n\n\t" +
                            "----------------------------------------------------------\n\t" +
                            "Application " + appName +" is running! Access URLs:\n\t" +
                            "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                            "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
                            "------------------------------------------------------------" + "\n"
            );
        };
    }
}
