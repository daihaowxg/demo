package io.github.daihaowxg.spi.impl;

import io.github.daihaowxg.spi.PaymentService;

// 实现 2：支付宝支付
public class AliPaymentService implements PaymentService {
    @Override
    public void pay() {
        System.out.println("使用支付宝支付");
    }
}
