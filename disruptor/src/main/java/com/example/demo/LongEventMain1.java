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

        // 这里使用了最基本的发布事件的方式
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        for (long i = 0; true; i++) {
            // 1. 申请下一个序列号
            long sequence = ringBuffer.next();
            try {
                // 2. 获取该序列号对应的事件对象
                LongEvent event = ringBuffer.get(sequence);
                // 3. 设置事件的值
                event.set(i);
            } finally {
                // 4. 发布事件
                ringBuffer.publish(sequence);
            }
            Thread.sleep(1000);
        }
    }
}
