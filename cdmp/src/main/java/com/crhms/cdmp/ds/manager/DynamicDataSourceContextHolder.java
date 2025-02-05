package com.crhms.cdmp.ds.manager;

/**
 * 线程本地存储数据源 key
 *
 * @author wxg
 * @since 2025/2/5
 */
public class DynamicDataSourceContextHolder {

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    public static String getDataSourceKey() {
        return CONTEXT_HOLDER.get();
    }

    public static void setDataSourceKey(String key) {
        CONTEXT_HOLDER.set(key);
    }

    public static void clearDataSourceKey() {
        CONTEXT_HOLDER.remove();
    }
}
