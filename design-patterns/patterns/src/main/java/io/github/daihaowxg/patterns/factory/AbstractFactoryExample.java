package io.github.daihaowxg.demo.designpattern.factory;

/**
 * @author wxg
 * @since 2025/3/7
 */
// 1. 定义手机接口（产品1）
interface Phonee {
    void call();
}

// 2. 定义充电器接口（产品2）
interface Charger {
    void charge();
}

// 3. 具体产品（iPhone）
class IPhonee implements Phonee {
    public void call() {
        System.out.println("使用 iPhone 打电话");
    }
}

class IPhoneCharger implements Charger {
    public void charge() {
        System.out.println("使用 iPhone 充电器充电");
    }
}

// 4. 具体产品（Huawei）
class HuaweiPhonee implements Phonee {
    public void call() {
        System.out.println("使用 Huawei 手机打电话");
    }
}

class HuaweiCharger implements Charger {
    public void charge() {
        System.out.println("使用 Huawei 充电器充电");
    }
}

// 5. 抽象工厂接口（可以创建多个产品）
interface DeviceFactory {
    Phonee createPhone();
    Charger createCharger();
}

// 6. 具体工厂（生产 iPhone + 充电器）
class IPhoneeFactory implements DeviceFactory {
    public Phonee createPhone() {
        return new IPhonee();
    }
    public Charger createCharger() {
        return new IPhoneCharger();
    }
}

// 7. 具体工厂（生产 Huawei 手机 + 充电器）
class HuaweiFactory implements DeviceFactory {
    public Phonee createPhone() {
        return new HuaweiPhonee();
    }
    public Charger createCharger() {
        return new HuaweiCharger();
    }
}

// 8. 测试
public class AbstractFactoryExample {
    public static void main(String[] args) {
        DeviceFactory factory = new HuaweiFactory();
        Phonee Phonee = factory.createPhone();
        Charger charger = factory.createCharger();
        Phonee.call();        // 输出: 使用 Huawei 手机打电话
        charger.charge();    // 输出: 使用 Huawei 充电器充电
    }
}

