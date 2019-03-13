package com.example.liujian.retrofitdemo.proxy;

/**
 * Created by liujian on 2017/7/25.
 */

public class RealSubject implements Subject{

    @Override
    public void rent() {
        System.out.println("I want to rent my house");
    }

    @Override
    public void hello(String str) {
        System.out.println("hello: " + str);
    }
}
