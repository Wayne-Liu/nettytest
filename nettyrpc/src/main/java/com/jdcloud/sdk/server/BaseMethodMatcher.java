package com.jdcloud.sdk.server;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public abstract class BaseMethodMatcher implements MethodMatcher {

    private final ConcurrentMap<MethodMatchInput, MethodMatchOutput> cache = Maps.newConcurrentMap();

    @Override
    public MethodMatchOutput selectOneBestMatchMethod(MethodMatchInput input) {

        return cache.computeIfAbsent(input, in -> {
            try {
                MethodMatchOutput output = new MethodMatchOutput();
                Class<?> interfaceClass = Class.forName(in.getInterfaceName());
                //获取宿主类信息
                HostClassMethodInfo info = findHostClassMethodInfo(interfaceClass);
                List<Method> targetMethods = Lists.newArrayList();
                ReflectionUtils.doWithMethods(info.getHostUserClass(), targetMethods::add, method -> {
                    String methodName = method.getName();
                    Class<?> declaringClass = method.getDeclaringClass();
                    List<Class<?>> inputParameterTypes = Optional.ofNullable(in.getMethodArgumentSignatures())
                            .map(mas -> {
                                List<Class<?>> list = Lists.newArrayList();
                                mas.forEach(ma -> list.add(ClassUtils.resolveClassName(ma, null)));
                                return list;
                            }).orElse(Lists.newArrayList());
                    output.setParameterTypes(inputParameterTypes);
                    //如果传入了参数签名列表，优先使用参数签名列表类型进行匹配
                    if (!inputParameterTypes.isEmpty()) {
                        List<Class<?>> parameterTypes = Lists.newArrayList(method.getParameterTypes());
                        return Objects.equals(methodName, in.getMethodName()) &&
                                Objects.equals(info.getHostUserClass(), declaringClass) &&
                                Objects.equals(parameterTypes, inputParameterTypes);
                    }
                    //如果没有传入参数签名列表，那么使用参数的数量进行匹配
                    if (in.getMethodArgumentArraySize() > 0) {
                        List<Class<?>> parameterTypes = Lists.newArrayList(method.getParameterTypes());
                        return Objects.equals(methodName, in.getMethodName()) &&
                                Objects.equals(info.getHostUserClass(), declaringClass) &&
                                in.getMethodArgumentArraySize() == parameterTypes.size();
                    }
                    //如果参数签名列表和参数列表都没有传入，那么只能通过方法名称和方法实例的宿主类型进行匹配
                    return Objects.equals(methodName, in.getMethodName()) &&
                            Objects.equals(info.getHostUserClass(), declaringClass);

                });
                if (targetMethods.size() != 1) {
                    throw new MethodMatchException(String.format("查找到的目标方法数量不等于1，interface:%s,method:%s",
                            in.getInterfaceName(),in.getMethodName()));
                }
                Method targetMethod = targetMethods.get(0);
                output.setTargetClass(info.getHostClass());
                output.setTargetMethod(targetMethod);
                output.setTargetUserClass(info.getHostUserClass());
                output.setTarget(info.getHostTarget());
                return output;

            } catch (Exception e) {
                log.error("查找匹配度最高的方法失败,输入参数:{}", JSON.toJSONString(in), e);
                if (e instanceof MethodMatchException) {
                    throw (MethodMatchException) e;
                } else {
                    throw new MethodMatchException(e);
                }
            }
        });
    }

    //获取宿主类信息
    abstract HostClassMethodInfo findHostClassMethodInfo(Class<?> interfaceClass);
}
