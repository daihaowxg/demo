package io.github.daihaowxg.demo;


import javassist.ClassPool;
import javassist.CtClass;

import java.util.ArrayList;
import java.util.List;

/**
 * 方法区/元空间溢出：动态生成大量类，导致方法区/元空间溢出
 */
public class MetaspaceOOM {

    /**
     * java -XX:MaxMetaspaceSize=10m -XX:-UseCompressedClassPointers -XX:+PrintGCDetails -XX:+PrintGCDateStamps com.example.demo.MetaspaceOOM
     * -XX:MaxMetaspaceSize=10m 限制 Metaspace（元空间）的最大大小为 10MB。
     * -XX:-UseCompressedClassPointers 禁用压缩类指针，以减少元空间使用量。启用压缩指针时，类的 metadata 可能会被存储在 Compressed Class Space 里，而不是 Metaspace
     * -XX:+PrintGCDetails 打印 GC 日志 这样可以帮助分析 Metaspace 是否真的导致了 OOM，或者是其他 GC 相关问题。
     * -XX:+PrintGCDateStamps 打印 GC 时间戳，这样 GC 日志中的时间戳就可以与实际时间进行比较，以确定 GC 是否在 Metaspace 中发生。
     */
    public static void main(String[] args) throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        List<CtClass> classList = new ArrayList<>(); // 保存类引用，防止 GC 回收
        for (int i = 0; ; i++) {
            CtClass generatedClass = classPool.makeClass("com.example.GeneratedClass" + i);
            classList.add(generatedClass); // 持有类引用
            generatedClass.toClass();
            System.out.println("Generated class: " + i);
        }
    }
}
