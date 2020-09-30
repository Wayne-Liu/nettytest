package com.jdcloud.sdk.server.contract;

import org.springframework.stereotype.Service;

@Service
public class DefaultHelloService implements HelloService {
    @Override
    public String sayHello(String name) {
        return String.format("%s say hello!", name);
    }
}
