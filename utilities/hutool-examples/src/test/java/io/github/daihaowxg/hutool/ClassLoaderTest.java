package io.github.daihaowxg.hutool;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

public class ClassLoaderTest {

    static ClassLoader classLoader;

    @BeforeAll
    public static void setUp() {
        classLoader = Thread.currentThread().getContextClassLoader();
        System.out.println("当前线程的类加载器: " + classLoader);
    }


    @Test
    public void getResource() {
        // 获取单个资源的URL
        URL resource = classLoader.getResource("config/app.properties");
        getClass().getResource("/config/app.properties");
        if (resource != null) {
            System.out.println("资源URL: " + resource);
        } else {
            System.out.println("资源未找到");
        }
    }

    @Test
    public void getResources() {
        // 获取多个资源的URL
        try {
            Enumeration<URL> resources = classLoader.getResources("config/app.properties");
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                System.out.println("资源URL: " + resource);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getResourceAsStream() {
        // 把资源文件加载为输入流
        try (InputStream inputStream = classLoader.getResourceAsStream("config/app.properties")) {
            if (inputStream != null) {
                System.out.println("资源已成功加载");
            } else {
                System.out.println("资源加载失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
