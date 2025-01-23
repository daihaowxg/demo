package com.example.demo;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

public class LongEventMain1 {
    public static void main(String[] args) throws InterruptedException {
        int bufferSize = 1024;

        // 初始化 Disruptor
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(LongEvent::new, bufferSize, DaemonThreadFactory.INSTANCE);
        // 设置 Disruptor 的事件处理器
        disruptor.handleEventsWith(new LongEventHandler());
        // 启动 Disruptor
        disruptor.start();

        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        for (long i = 0; true; i++) {
            long sequence = ringBuffer.next();
            try {
                LongEvent event = ringBuffer.get(sequence);
                event.set(i);
            } finally {
                ringBuffer.publish(sequence);
            }
            Thread.sleep(1000);
        }
    }
}
