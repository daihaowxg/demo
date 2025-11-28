package io.github.daihaowxg.proxy.cglib;

public class CglibProxyDemo {
    public static void main(String[] args) {
        ProductService target = new ProductService();
        ProductService proxy = (ProductService) new CglibProxyHandler(target).createProxy();
        proxy.addProduct("手机");
    }
}