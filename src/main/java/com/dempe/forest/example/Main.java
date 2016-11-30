package com.dempe.forest.example;

import com.dempe.forest.client.proxy.JdkProxyFactory;
import com.dempe.forest.core.annotation.Action;
import com.dempe.forest.core.annotation.Export;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/29
 * Time: 17:56
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(TestImpl1.class);
        enhancer.setCallback(new MethodInterceptor(){

            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                Export annotation = method.getAnnotation(Export.class);
                System.out.println("uri:"+annotation.uri());


                return methodProxy.invokeSuper(o, objects);
            }
        });

        Test demo = (Test) enhancer.create();

        String hello = demo.say("hello");

        System.out.println("result:"+hello);


    }

    public static void test(){
        Test proxy = new JdkProxyFactory().getProxy(Test.class, new TestInvocationHandler(new TestImpl1()));
        String hello = proxy.say("hello");
        System.out.println(hello);
    }


}

class TestInvocationHandler implements InvocationHandler {

    private Object target;

    public TestInvocationHandler(Object target) {
        this.target = target;
    }


    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        return method.invoke(target, objects);
    }
}

