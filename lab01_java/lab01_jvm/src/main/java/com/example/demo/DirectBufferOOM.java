package com.example.demo;

import java.nio.ByteBuffer;

/**
 * 直接内存溢出：NIO 直接分配大量 ByteBuffer，导致直接内存耗尽
 */
public class DirectBufferOOM {

    /**
     * java -XX:MaxDirectMemorySize=10m DirectBufferOOM
     */
    public static void main(String[] args) {
        while (true) {
            ByteBuffer.allocateDirect(1024 * 1024 * 10); // 10MB DirectBuffer
        }
    }
}
