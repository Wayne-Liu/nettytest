package com.jdcloud.sdk;

import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.jdcloud.sdk.server.BaseMethodMatcher;
import com.jdcloud.sdk.server.contract.DefaultHelloService;
import com.jdcloud.sdk.server.contract.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * Unit test for simple App.
 */

//@RunWith(SpringRunner.class)
//@SpringBootTest

@Slf4j
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void doWithMethods() {
        ReflectionUtils.MethodCallback methodCallback = new ReflectionUtils.MethodCallback() {
            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                System.out.println("methodCallback Method is:"+method.getName());
            }
        };

        ReflectionUtils.MethodFilter methodFilter = new ReflectionUtils.MethodFilter() {
            @Override
            public boolean matches(Method method) {
                if (method.getName().contains("selectOneBestMatchMethod")) {
                    System.out.println("包含selectOneBestMatchMethod");
                    return true;
                }
                return false;
            }
        };
        ReflectionUtils.doWithMethods(BaseMethodMatcher.class,methodCallback, methodFilter);
    }

    @Test
    public void reflect() {
        Function<Integer,Integer> mappingFunction = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer+8;
            }
        };

        ConcurrentMap<Integer,Integer> concurrentMap = Maps.newConcurrentMap();
        for (int i=0;i<5;i++) {
            concurrentMap.computeIfAbsent(i, in -> {
                return in + 5;
            });
        }

        for (int i=0;i<8;i++) {
            concurrentMap.computeIfAbsent(i, mappingFunction);
        }

        for (int i=0;i<10;i++) {
            log.info("key={},value={}",i,concurrentMap.get(i));
        }
        Class<?> interfs = DefaultHelloService.class;
        Class<?> claz = interfs.getDeclaringClass();
        log.info("simpleName:{},DeclareName:{}",claz.getSimpleName(),claz.getCanonicalName());


    }
}
