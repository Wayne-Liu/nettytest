package com.jdclud.sdk.server;

import com.jdclud.sdk.common.Calculator;

public class CalculatorImpl implements Calculator {
    @Override
    public int add(int a, int b) {
        return a + b;
    }
}
