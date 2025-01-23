package com.example.demo;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;

public class LongEventMain {
    public static void main(String[] args) throws InterruptedException {
        // 指定环形缓冲区的大小，必须是2的幂，以便通过位运算高效计算索引。
        int bufferSize = 1024;

        // 创建一个 Disruptor 实例，指定事件工厂、缓冲区大小和线程工厂。
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(
                LongEvent::new, // 或者 new LongEventFactory()
                bufferSize,
                DaemonThreadFactory.INSTANCE
        );

        // 为 Disruptor 设置事件处理逻辑。
        disruptor.handleEventsWith(new LongEventHandler());

        // 启动 Disruptor，开始处理事件。
        disruptor.start();

        // 获取 RingBuffer 对象，用于发布事件。
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        // 创建一个 ByteBuffer，用于存储模拟数据。
        ByteBuffer bb = ByteBuffer.allocate(8);

        // 模拟持续发布事件，每秒发布一个。
        for (int i = 0; true; i++) {
            // 将当前的循环计数器值写入 ByteBuffer 的第一个位置。
            bb.putLong(0, i);

            // 发布事件到 RingBuffer。
            // publishEvent 方法使用 Lambda 表达式，将 ByteBuffer 的值传递给事件。
            ringBuffer.publishEvent((event, sequence, byteBuffer) -> event.set(bb.getLong(0)), bb);

            // 模拟延迟，每次发布事件后暂停 1 秒。
            Thread.sleep(1000);
        }
    }
}
