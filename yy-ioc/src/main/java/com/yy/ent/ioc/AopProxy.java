package com.yy.ent.ioc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/19
 * Time: 19:57
 * To change this template use File | Settings | File Templates.
 */
public class AopProxy<T> implements InvocationHandler {

    //接口的实现类，
    private Object targetCreator;

    //保存代理类的实现
    private Object proxyInstance = null;

    private Aop aop;

    public AopProxy(Object targetCreator, Aop aop) {
        this.targetCreator = targetCreator;
        this.aop = aop;
    }

    /**
     * 该方法负责集中处理动态代理类上的所有方法调用。
     * 第一个参数既是代理类实例，
     * 第二个参数是被调用的方法对象
     * 第三个方法是调用参数。调用处理器根据这三个参数进行预处理或分派到委托类实例上发射执行
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        Object result = null;
        aop.before(method, args);
        /**
         * 动态代理类$Proxy0调用method方法时会调用它自己的相对应的method方法，
         * 而它自己的方法里面调用的是super.h.invoke(this, , )，也就是父类Proxy的h的invoke方法，
         * 也就是ReportCreatorProxy类的invoke方法。
         * 所以，invoke(Object proxy, Method m, Object[] args)中的proxy实际上就是动态代理类$Proxy0，
         * 如果你将其强转成XXXXXInterface然后调用它的方法，然后它就会调用super.h.invoke(this, , )，这样就会死循环。
         */
        result = method.invoke(this.targetCreator, args);
        aop.after(method, args);
        return result;
    }


    /**
     * 静态工厂方法，用于获取动态代理实例
     *
     * @param target
     * @return
     */
    public static Object getProxyInstance(Object target, Aop aop) {
        Class<?> targetClass = target.getClass();
        /*
         * loader:  要代理类的类加载器
    	 * interfaces:  要代理类所实现的所有的接口
    	 * handler: 转发方法调用的调用处理类实例，即代理类
    	 */
        ClassLoader loader = targetClass.getClassLoader();
        Class<?>[] interfaces = targetClass.getInterfaces();
        AopProxy<Object> handler = new AopProxy<Object>(target, aop);
        if (handler.proxyInstance == null) {
            // 创建并返回动态代理类实例
            handler.proxyInstance = Proxy.newProxyInstance(loader, interfaces, handler);
        }
        return handler.proxyInstance;
    }


}
