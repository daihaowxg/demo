package io.github.daihaowxg.demo;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 直接内存溢出：NIO 直接分配大量 ByteBuffer，导致直接内存耗尽
 */
public class DirectBufferOOM {
    public static void main(String[] args) {
        List<ByteBuffer> buffers = new ArrayList<>();
        while (true) {
            buffers.add(ByteBuffer.allocateDirect(1024 * 1024 * 1)); // 1MB DirectBuffer
            System.out.println("Allocated direct buffer: " + buffers.size() + "MB");
        }
    }
}
