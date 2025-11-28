package io.github.daihaowxg.demo;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;

public class LongEventMain2 {
    public static void main(String[] args) throws InterruptedException {
        int bufferSize = 1024;

        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(LongEvent::new, bufferSize, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(new LongEventHandler());
        disruptor.start();

        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        ByteBuffer bb = ByteBuffer.allocate(bufferSize);
        for (long i = 0; true; i++) {
            bb.putLong(0, i);
            ringBuffer.publishEvent((event, sequence) -> event.set(bb.getLong(0)));
            Thread.sleep(1000);
        }
    }
}
