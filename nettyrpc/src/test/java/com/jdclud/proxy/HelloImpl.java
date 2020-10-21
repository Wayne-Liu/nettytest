package com.jdclud.proxy;

public class HelloImpl implements Hello {
    @Override
    public void hi(String msg) {
        System.out.println("hello " + msg);
    }
}
