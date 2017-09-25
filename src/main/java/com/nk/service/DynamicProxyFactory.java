package com.nk.service;

import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DynamicProxyFactory {

    public Object create(Object origin, InvocationHandler invocationHandler) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(origin.getClass());
        enhancer.setCallback(new net.sf.cglib.proxy.InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                return invocationHandler.invoke(o, method, objects);
            }
        });


        try {
            return enhancer.create();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
