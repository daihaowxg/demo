package io.github.daihaowxg.openrewrite.legacy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Vector;

/**
 * 使用旧式 API 和模式的服务类
 * 用于演示 OpenRewrite 的 API 升级能力
 */
@Service
public class OldStyleService {
    
    // 字段注入（不推荐，应该使用构造器注入）
    @Autowired
    private OldStyleRepository repository;
    
    // 使用过时的 Date API（应该使用 java.time）
    public Date getCurrentDate() {
        return new Date();
    }
    
    // 使用过时的 Vector（应该使用 ArrayList）
    public Vector<String> getItems() {
        Vector<String> items = new Vector<>();
        items.add("item1");
        items.add("item2");
        items.add("item3");
        return items;
    }
    
    // 使用旧式的 for 循环（可以用 Stream API）
    public int sumNumbers(int[] numbers) {
        int sum = 0;
        for (int i = 0; i < numbers.length; i++) {
            sum += numbers[i];
        }
        return sum;
    }
    
    // 使用显式类型（Java 10+ 可以使用 var）
    public void processData() {
        String message = "Processing data...";
        System.out.println(message);
        
        StringBuilder builder = new StringBuilder();
        builder.append("Result: ");
        builder.append(repository.getData());
        System.out.println(builder.toString());
    }
    
    // 可以使用 instanceof 模式匹配（Java 16+）
    public String getObjectType(Object obj) {
        if (obj instanceof String) {
            String str = (String) obj;
            return "String: " + str.length();
        } else if (obj instanceof Integer) {
            Integer num = (Integer) obj;
            return "Integer: " + num;
        }
        return "Unknown";
    }
}

@Service
class OldStyleRepository {
    public String getData() {
        return "Sample Data";
    }
}
