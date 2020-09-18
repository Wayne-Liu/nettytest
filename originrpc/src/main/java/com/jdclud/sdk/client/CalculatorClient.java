package com.jdclud.sdk.client;

import com.jdclud.sdk.common.Calculator;
import com.jdclud.sdk.common.CalculatorRemoteImpl;
import com.jdclud.sdk.server.CalculatorImpl;

import java.util.concurrent.Callable;

public class CalculatorClient {

    public static void main(String[] args) {
        Calculator calculator = new CalculatorRemoteImpl();

        int result = calculator.add(3, 55);

        System.out.println("{}+{}的计算结果为："+result);
    }

}
