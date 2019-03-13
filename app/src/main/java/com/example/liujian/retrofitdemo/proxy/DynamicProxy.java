package com.example.liujian.retrofitdemo.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by liujian on 2017/7/25.
 */

public class DynamicProxy implements InvocationHandler{

    private Object subject;

    public DynamicProxy(Object subject) {
        this.subject = subject;
    }

    @Override
    public Object invoke(Object object, Method method, Object[] args) throws Throwable {

        System.out.println("before rent house");

        System.out.println("Method:" + method);

        method.invoke(subject, args);

        System.out.println("after rent house");

        return null;
    }
}
