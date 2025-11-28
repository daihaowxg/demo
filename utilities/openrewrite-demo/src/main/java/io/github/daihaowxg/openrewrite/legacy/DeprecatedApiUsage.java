package io.github.daihaowxg.openrewrite.legacy;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 使用废弃 API 的示例类
 * 用于演示 OpenRewrite 如何自动替换废弃的 API
 */
public class DeprecatedApiUsage {
    
    /**
     * 使用废弃的 URLEncoder.encode(String) 方法
     * 应该使用 URLEncoder.encode(String, Charset)
     */
    public String encodeUrl(String url) throws UnsupportedEncodingException {
        // 旧式写法：需要处理异常
        return URLEncoder.encode(url, "UTF-8");
    }
    
    /**
     * 使用废弃的 URLDecoder.decode(String) 方法
     */
    public String decodeUrl(String url) throws UnsupportedEncodingException {
        return URLDecoder.decode(url, "UTF-8");
    }
    
    /**
     * 使用 new Integer() 构造器（已废弃）
     * 应该使用 Integer.valueOf()
     */
    public Integer createInteger(int value) {
        return new Integer(value);
    }
    
    /**
     * 使用 new Long() 构造器（已废弃）
     */
    public Long createLong(long value) {
        return new Long(value);
    }
    
    /**
     * 使用 new Double() 构造器（已废弃）
     */
    public Double createDouble(double value) {
        return new Double(value);
    }
    
    /**
     * 使用 new Boolean() 构造器（已废弃）
     */
    public Boolean createBoolean(boolean value) {
        return new Boolean(value);
    }
    
    /**
     * 字符串编码的旧式写法
     */
    public byte[] encodeString(String text) throws UnsupportedEncodingException {
        return text.getBytes("UTF-8");
    }
    
    /**
     * 字符串解码的旧式写法
     */
    public String decodeBytes(byte[] bytes) throws UnsupportedEncodingException {
        return new String(bytes, "UTF-8");
    }
}
