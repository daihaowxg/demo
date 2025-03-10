package com.example.spi;

import java.util.ServiceLoader;

public class SPIDemo {
    public static void main(String[] args) {
        // 通过 ServiceLoader 加载所有 PaymentService 实现
        ServiceLoader<PaymentService> services = ServiceLoader.load(PaymentService.class);

        // 遍历所有实现类并执行
        for (PaymentService service : services) {
            service.pay();
        }
    }
}
