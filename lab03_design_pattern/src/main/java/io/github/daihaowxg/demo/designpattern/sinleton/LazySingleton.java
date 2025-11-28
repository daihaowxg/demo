package io.github.daihaowxg.demo.designpattern.sinleton;

/**
 * 懒汉式（synchronized 方法，线程安全）
 *
 * @author wxg
 * @since 2025/3/6
 */
class LazySingletonSynchronized {
    private static LazySingletonSynchronized instance;

    private LazySingletonSynchronized() {
    }

    public static synchronized LazySingletonSynchronized getInstance() {
        if (instance == null) {
            instance = new LazySingletonSynchronized();
        }
        return instance;
    }
}
