package com.jdclud.sdk.server;

public interface MethodMatcher {
    MethodMatchOutput selectOneBestMatchMethod(MethodMatchInput input);

}
