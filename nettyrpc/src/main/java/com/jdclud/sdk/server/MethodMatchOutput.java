package com.jdclud.sdk.server;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.List;

@Data
public class MethodMatchOutput {

    //目标方法实例
    private Method targetMethod;
    //目标实现类
    private Class<?> targetClass;
    //宿主类
    private Class<?> targetUserClass;
    //宿主类bean实例
    private Object target;
    //方法参数类型列表
    private List<Class<?>> parameterTypes;

}
