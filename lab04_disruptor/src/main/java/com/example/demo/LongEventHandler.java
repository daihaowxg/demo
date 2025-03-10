package com.example.demo;

import com.lmax.disruptor.EventHandler;

/**
 * 定义事件处理器
 */
public class LongEventHandler implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("Event: " + event.toString());
    }
}
