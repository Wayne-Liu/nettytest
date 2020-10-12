package com.jdcloud.sdk;

import static org.junit.Assert.assertTrue;

import com.jdcloud.sdk.server.BaseMethodMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * Unit test for simple App.
 */

//@RunWith(SpringRunner.class)
//@SpringBootTest

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
}
