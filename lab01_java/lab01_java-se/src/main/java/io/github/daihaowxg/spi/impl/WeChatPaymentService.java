package io.github.daihaowxg.spi.impl;

import io.github.daihaowxg.spi.PaymentService;

// 实现 1：微信支付
public class WeChatPaymentService implements PaymentService {
    @Override
    public void pay() {
        System.out.println("使用微信支付");
    }
}
