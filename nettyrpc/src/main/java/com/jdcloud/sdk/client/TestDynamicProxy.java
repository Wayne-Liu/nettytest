package com.jdcloud.sdk.client;

import com.alibaba.fastjson.JSON;
import com.jdcloud.sdk.server.contract.HelloService;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TestDynamicProxy {

    public static void main(String[] args) {
        Class<HelloService> interfaceKlass = HelloService.class;
        InvocationHandler handler = new HelloServiceImpl(interfaceKlass);
        HelloService helloService = (HelloService) Proxy.newProxyInstance(interfaceKlass.getClassLoader(),
                new Class[]{interfaceKlass}, handler);
        System.out.println(helloService.sayHello("throw"));
    }

    @RequiredArgsConstructor
    private static class HelloServiceImpl implements InvocationHandler {

        private final Class<?> interfaceKlass;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return String.format("[%s#%s]方法被调用，参数列表:%s",interfaceKlass.getName(),
                    method.getName(), JSON.toJSON(args));
        }
    }

}
