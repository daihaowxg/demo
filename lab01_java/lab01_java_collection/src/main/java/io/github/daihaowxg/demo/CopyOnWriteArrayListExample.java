package io.github.daihaowxg.demo;

import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayListExample {
    public static void main(String[] args) {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();

        // 添加元素
        list.add("A");
        list.add("B");
        list.add("C");

        // 读操作
        System.out.println("List: " + list); // List: [A, B, C]

        // 迭代器（基于快照）
        for (String s : list) {
            System.out.println(s); // 依次逐行输出 A、B、C
            list.add("D"); // 不会影响当前迭代
        }

        System.out.println("Final List: " + list); // Final List: [A, B, C, D, D, D]
    }
}