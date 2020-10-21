package com.jdclud.reflect;

public class PrivateTest {

    public String hello(String name){
        return "hello " + name;
    }

    public String hello(){
        return "hello haha";
    }

    private String good() {
        return "good boy";
    }

    public static String myself(String my) {
        return  "myself "+ my;
    }
}
