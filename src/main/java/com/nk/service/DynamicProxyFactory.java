package com.nk.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class DynamicProxyFactory {

    public Object create(Object origin, InvocationHandler invocationHandler) {
        return Proxy.newProxyInstance(
            origin.getClass().getClassLoader(),
            origin.getClass().getInterfaces(),
            invocationHandler
        );
    }

}
