package com.springtest.demo.util;

public class LambdaLogicChain<T> {

    public LambdaLogicChain() {}

    @SafeVarargs
    public final T process(LambdaChainUnit<T> ...units) {
        for (var unit : units) {
            T val = unit.condition();
            if (val != null)
                return val;
        }
        return null;
    }


}
