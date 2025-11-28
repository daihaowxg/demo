package io.github.daihaowxg.demo;

import java.util.HashMap;
import java.util.Map;

/**
 * 垃圾回收耗时过长：创建大量短生命周期对象，导致 GC 频繁运行
 */
public class GCOverheadTest {

    /**
     * java -Xmx10m -XX:+UseParallelGC GCOverheadTest
     */
    public static void main(String[] args) {
        Map<Integer, String> map = new HashMap<>();
        int i = 0;
        while (true) {
            map.put(i++, "test");
        }
    }
}
