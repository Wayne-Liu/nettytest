package com.jdcloud.sdk.server;

public interface MethodMatcher {
    MethodMatchOutput selectOneBestMatchMethod(MethodMatchInput input);

}
