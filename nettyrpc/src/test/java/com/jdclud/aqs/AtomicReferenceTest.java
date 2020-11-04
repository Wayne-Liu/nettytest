package com.jdclud.aqs;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceTest {

    public static void main(String[] args) {

        String value = "hello world";
        ReferenceObject object = new ReferenceObject(value);
        AtomicReference<ReferenceObject> atomicReference = new AtomicReference<>(object);
        atomicReference.compareAndSet(object, new ReferenceObject("Hello guy!"));
        System.out.println(atomicReference.get().getValue());
        atomicReference.compareAndSet(object, new ReferenceObject("Hello girl!"));
        System.out.println(atomicReference.get().getValue());

        // 顺道测试一下Integer的常量池
        //可以插播Integer常量池的特点, new Integer是在堆上生成一个新对象,
        //只有Integer对象赋值(编译时转成Integer.valueOf())或Integer.valueof且默认数值在-128 - 127才会用到常量池.
        AtomicReference<Integer> integerNewReference = new AtomicReference(new Integer(10));
        integerNewReference.compareAndSet(new Integer(10), new Integer(-10));
        System.out.println(integerNewReference.get().intValue());

        AtomicReference<Integer> integerValeOfReference = new AtomicReference(Integer.valueOf(10));
        integerValeOfReference.compareAndSet(new Integer(10), new Integer(-10));
        System.out.println(integerValeOfReference.get().intValue());

        integerValeOfReference.compareAndSet(Integer.valueOf(10), new Integer(-10));
        System.out.println(integerValeOfReference.get().intValue());
    }

    static class ReferenceObject {
        private String value;

        public ReferenceObject(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
