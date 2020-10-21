package com.jdclud.reflect;

import java.lang.reflect.Method;

public class Reflect {

    public static void main(String[] args) throws Exception {

        Class<String> stringClass = String.class;

        Method hashCodeMethod = stringClass.getDeclaredMethod("hashCode");

        String str= "hello, world";
        Object hashCode= hashCodeMethod.invoke(str);
        System.out.println( hashCode);

        //私有方法getDeclaredMethod获取不到
//        Class<?> privateClass = PrivateTest.class;
//        Method goodMethod = privateClass.getDeclaredMethod("good");
//
//        Object good = goodMethod.invoke(null);
//        System.out.println(good);

        //动态方法需要先实例化
        //Class<?> privateClass = PrivateTest.class;
        PrivateTest privateTest = new PrivateTest();

        Method helloMethod = privateTest.getClass().getDeclaredMethod("hello",String.class);
        String name = "wayne";
        Object hello = helloMethod.invoke(privateTest, name);
        System.out.println(hello);

        Method hahaMethod = privateTest.getClass().getDeclaredMethod("hello");
        Object hh = hahaMethod.invoke(privateTest);
        System.out.println("hhMethod:"+hh);


        //静态方法
        Class<?> privateClass = PrivateTest.class;
        Method myself = privateClass.getDeclaredMethod("myself", String.class);
        Object my = myself.invoke(privateClass, "me");
        System.out.println("static method:" + my);
    }
}
