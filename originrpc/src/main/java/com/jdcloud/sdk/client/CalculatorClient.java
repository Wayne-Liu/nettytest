package com.jdcloud.sdk.client;

import com.jdcloud.sdk.common.Calculator;
import com.jdcloud.sdk.common.CalculatorRemoteImpl;

public class CalculatorClient {

    public static void main(String[] args) {
        Calculator calculator = new CalculatorRemoteImpl();

        int result = calculator.add(3, 55);

        System.out.println("{}+{}的计算结果为："+result);
    }

}
