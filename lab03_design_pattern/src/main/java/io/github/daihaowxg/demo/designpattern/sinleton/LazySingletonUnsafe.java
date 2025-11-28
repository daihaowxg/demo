package io.github.daihaowxg.demo.designpattern.sinleton;

/**
 * 懒汉式（线程不安全）
 *
 * @author wxg
 * @since 2025/3/6
 */
public class LazySingletonUnsafe {
    private static LazySingletonUnsafe instance;

    private LazySingletonUnsafe() {
    }

    public static LazySingletonUnsafe getInstance() {
        if (instance == null) {
            instance = new LazySingletonUnsafe();
        }
        return instance;
    }
}
