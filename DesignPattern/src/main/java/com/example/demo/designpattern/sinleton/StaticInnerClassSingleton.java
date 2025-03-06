package com.example.demo.designpattern.sinleton;

/**
 * 静态内部类（线程安全，推荐）
 *
 * @author wxg
 * @since 2025/3/6
 */
class StaticInnerClassSingleton {
    private StaticInnerClassSingleton() {
    }

    public static StaticInnerClassSingleton getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final StaticInnerClassSingleton INSTANCE = new StaticInnerClassSingleton();
    }
}
