package io.github.daihaowxg.demo;

import java.util.ArrayList;
import java.util.List;


/**
 * 堆内存溢出：创建大量对象导致堆内存耗尽
 */
public class HeapOOM {
    static class OOMObject {}

    /**
     * 1、进入类路径 cd /Users/wxg/IdeaProjects/demo/lab01_java/lab01_jvm/target/classes
     * 2、执行命令 java -Xmx10m com.example.demo.HeapOOM
     * 3、解决方案是提高堆内存 java -Xmx512m com.example.demo.HeapOOM
     */
    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<>();
        while (true) {
            list.add(new OOMObject()); // 无限创建对象，消耗堆内存
        }
    }
}
