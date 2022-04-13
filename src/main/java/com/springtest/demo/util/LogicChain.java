package com.springtest.demo.util;

public class LogicChain<T> {


    private final Object[] params;
    public LogicChain(Object...params) {
        this.params = params;
    }


    @SafeVarargs
    public final T process(ChainUnit<T>... units) {
        for (ChainUnit<T> unit : units) {
            T val = unit.condition(params);
            if (val != null)
                return val;
        }
        return null;
    }
}




