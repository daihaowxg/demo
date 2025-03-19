package com.example.demo;


import javassist.ClassPool;

/**
 * 方法区/元空间溢出：动态生成大量类，导致方法区/元空间溢出
 */
public class MetaspaceOOM {

    /**
     * java -XX:MaxMetaspaceSize=10m MetaspaceOOM
     */
    public static void main(String[] args) throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        for (int i = 0; ; i++) {
            classPool.makeClass("com.example.GeneratedClass" + i).toClass();
        }
    }
}
