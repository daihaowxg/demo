package com.example.hutool;

import cn.hutool.core.io.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.File;

class FileUtilTest {


    @Test
    public void ls() {
        // 列出指定路径下的所有文件，传参是目录的绝对路径或者相对路径
        File[] files = FileUtil.ls("com/example/hutool");
        // 遍历文件数组，打印每个文件的名称
        for (File file : files) {
            // 判断是否是文件夹
            if (file.isDirectory()) {
                System.out.println("目录: " + file.getName());
            } else {
                System.out.println("文件: " + file.getName());
            }
        }
    }
}
