package com.jdcloud.sdk.server;

import com.jdcloud.sdk.common.Calculator;

public class CalculatorImpl implements Calculator {
    @Override
    public int add(int a, int b) {
        return a + b;
    }
}
