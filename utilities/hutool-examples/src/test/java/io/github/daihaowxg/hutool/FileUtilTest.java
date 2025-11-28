package io.github.daihaowxg.hutool;

import cn.hutool.core.io.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

class FileUtilTest {


    @Test
    public void ls() {
        // 列出指定路径下的所有文件，传参是目录的绝对路径或者相对路径
        // 注意这里的路径分隔符要是正斜杠（/）
        // File[] files = FileUtil.ls("com/example/hutool");
        File[] files = FileUtil.ls("dir01/son01");
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

    @Test
    public void isEmpty() {
        // 注意这里的传参是相对目录，相对的是当前项目的根目录，且应该使用正斜杠（/）作为路径分隔符
        File dir = new File("src/test/java/com/example/hutool");
        boolean isEmpty = FileUtil.isEmpty(dir);
        if (isEmpty) {
            System.out.println("目录是空的");
        } else {
            System.out.println("目录不是空的");
        }
    }

    @Test
    void isDirEmpty() {
        // File dir = new File("src/test/java/com/example/hutool");
        File dir = new File("src/test/java/com/example/empty");
        boolean dirEmpty = FileUtil.isDirEmpty(dir);
        if (dirEmpty) {
            System.out.println("目录是空的");
        } else {
            System.out.println("目录不是空的");
        }
    }

    @Test
    void loopFiles() {
        List<File> files = FileUtil.loopFiles("dir01");
        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("目录: " + file.getName());
            } else {
                System.out.println("文件: " + file.getName());
            }
        }
    }

    @Test
    void listFileNames() {
        List<String> files = FileUtil.listFileNames("dir01");
        for (String fileName : files) {
            System.out.println("文件名: " + fileName);
        }
    }
}
