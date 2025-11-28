package io.github.daihaowxg.openrewrite.legacy;

import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

/**
 * 这是一个格式混乱、代码风格不统一的示例类
 * 用于演示 OpenRewrite 的自动格式化和代码清理能力
 */
public class UnformattedCode {

    private String name;
    private int age;
    private List<String> hobbies;

    // 格式混乱的构造器
    public UnformattedCode(String name, int age, List<String> hobbies) {
        this.name = name;
        this.age = age;
        this.hobbies = hobbies;
    }

    // 冗余的布尔表达式
    public boolean isAdult() {
        return age >= 18;
    }

    // 低效的字符串拼接
    public String getDescription() {
        String result = "";
        result = result + "Name: " + name;
        result = result + ", Age: " + age;
        return result + ", Hobbies: " + hobbies.toString();
    }

    // 可以简化的条件判断
    public boolean hasHobby(String hobby) {
        return hobbies.contains(hobby);
    }

    // 冗余的 null 检查
    public int getHobbyCount() {
        if (hobbies != null) {
            if (!hobbies.isEmpty()) {
                return hobbies.size();
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    // 可以使用现代 Java 特性改进
    public List<String> getUpperCaseHobbies() {
        List<String> result = new ArrayList<>();
        for (String hobby : hobbies) {
            result.add(hobby.toUpperCase());
        }
        return result;
    }

    // 不必要的装箱
    public Integer calculateSomething(Integer a, Integer b) {
        return Integer.valueOf(a.intValue() + b.intValue());
    }

    // 格式混乱的方法
    public void printInfo() {
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
    }

    // Getters and Setters (格式不统一)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getHobbies()
    {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies)
    {
        this.hobbies = hobbies;
    }
}
