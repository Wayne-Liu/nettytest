package com.jdclud.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//@Data
//@NoArgsConstructor
@AllArgsConstructor
public class HelloProxy implements InvocationHandler {
    private Object proxied = null;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("hello proxy");
        return method.invoke(proxied, args);
    }

    public static void main(String[] args) {

        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        Hello hello = (Hello) Proxy.newProxyInstance(Hello.class.getClassLoader(),new Class[]{Hello.class},new HelloProxy(new HelloImpl()));

        System.out.println(hello.getClass());
        hello.hi("world");
    }
}
