package io.github.daihaowxg.serialize;

import lombok.Setter;

import java.io.*;

class Person implements Serializable {
    private static final long serialVersionUID = 1L; // 版本控制，防止不兼容
    private String name;
    private int age;
    transient private String address; // 不会被序列化
    public static int count = 0; // 不会被序列化
    @Setter
    private String sex;

    public Person(String name, int age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }


    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}

public class SerializationExample {
    public static void main(String[] args) {
        Person person = new Person("张三", 30, "北京");
        Person.count = 123;
        person.setSex("男");
        String filename = "person.ser";

        // 1. 序列化对象到文件中
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(person);
            System.out.println("对象已序列化：" + person);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. 从文件中反序列化对象
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Person deserializedPerson = (Person) ois.readObject();
            System.out.println("反序列化对象：" + deserializedPerson);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
