package io.github.daihaowxg.demo.designpattern.factory;


// 1. 定义手机接口（产品）
interface Phone {
    void call();
}

// 2. 具体产品（不同品牌的手机）
class IPhone implements Phone {
    public void call() {
        System.out.println("使用 iPhone 打电话");
    }
}

class HuaweiPhone implements Phone {
    public void call() {
        System.out.println("使用 Huawei 手机打电话");
    }
}

// 3. 定义工厂接口
interface PhoneFactory {
    Phone createPhone();
}

// 4. 具体工厂（创建不同品牌的手机）
class IPhoneFactory implements PhoneFactory {
    public Phone createPhone() {
        return new IPhone();
    }
}

class HuaweiPhoneFactory implements PhoneFactory {
    public Phone createPhone() {
        return new HuaweiPhone();
    }
}

// 5. 测试
public class FactoryMethodExample {
    public static void main(String[] args) {
        PhoneFactory factory = new HuaweiPhoneFactory();
        Phone phone = factory.createPhone();
        phone.call(); // 输出: 使用 Huawei 手机打电话

        factory = new IPhoneFactory();
        phone = factory.createPhone();
        phone.call(); // 输出: 使用 iPhone 打电话

        // 8. 新增一个小米手机
        factory = new XiaomiPhoneFactory();
        phone = factory.createPhone();
        phone.call();
    }
}


// 6. 新增一个手机品牌
class XiaomiPhone implements Phone {
    public void call() {
        System.out.println("使用 Xiaomi 手机打电话");
    }
}

// 7. 新增一个手机品牌对应的工厂
class XiaomiPhoneFactory implements PhoneFactory {
    public Phone createPhone() {
        return new XiaomiPhone();
    }
}

