package io.github.daihaowxg.patterns.singleton;

/**
 * 饿汉式单例模式(线程安全)
 *
 * @author wxg
 * @since 2025/3/6
 */
public class EagerSingleton {
    private static final EagerSingleton INSTANCE = new EagerSingleton();

    private EagerSingleton() {
    }

    public static EagerSingleton getInstance() {
        return INSTANCE;
    }
}
