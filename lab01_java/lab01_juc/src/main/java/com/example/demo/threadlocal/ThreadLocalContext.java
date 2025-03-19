package com.example.demo.threadlocal;

/**
 * @author wxg
 * @since 2025/3/14
 */
public class ThreadLocalContext {
    private static final ThreadLocal<String> userHolder =  new ThreadLocal<>();


    public static void setUser(String user) {
        userHolder.set(user);
    }

    public static String getUser() {
        return userHolder.get();
    }
}
