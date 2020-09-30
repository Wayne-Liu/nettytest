package com.jdcloud.sdk.server;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

@Component
public class SpringMethodMatcher extends BaseMethodMatcher implements BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

    @Override
    HostClassMethodInfo findHostClassMethodInfo(Class<?> interfaceClass) {
        HostClassMethodInfo info = new HostClassMethodInfo();
        //通过接口类型获取实现类
        Object bean = beanFactory.getBean(interfaceClass);
        info.setHostTarget(bean);
        info.setHostClass(bean.getClass());
        info.setHostUserClass(ClassUtils.getUserClass(bean.getClass()));
        return info;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }
}
