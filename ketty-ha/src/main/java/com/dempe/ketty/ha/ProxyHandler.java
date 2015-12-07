package com.dempe.ketty.ha;

import com.dempe.ketty.common.access.AccessPolicy;
import com.dempe.ketty.ha.exception.IgnoreException;
import com.dempe.ketty.ha.listener.HAEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class ProxyHandler<T> implements InvocationHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProxyHandler.class);

    //接口的实现类，  
    private Object targetCreator;

    //实现HalbProxy的proxy类
    private Object halProxy;

    //保存代理类的实现
    private Object proxyInstance = null;

    private AccessPolicy strategy = new AccessPolicy();

    public ProxyHandler(Object targetCreator, Object halProxy, AccessPolicy strategy) {
        this.targetCreator = targetCreator;
        this.halProxy = halProxy;
        this.strategy = strategy;
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
        try {
            result = method.invoke(this.targetCreator, args);
        } catch (Exception ex) {
            if (ex.getCause() instanceof IgnoreException) {
                // no changeClient
            } else {
                ServerInfo info = (ServerInfo) this.targetCreator;
                String ip = info.getIp();
                int port = info.getPort();
                LOGGER.info("Thread name:{}, invoke exception ip:{}, port:{}, method:{}",
                        Thread.currentThread().getId(), ip, port, method.getName());
                strategy.addHit(ip + ":" + port);
                boolean ret = strategy.getFilterResult(ip + ":" + port);
                if (!ret) {
                    changeClient(this.targetCreator);
                }
            }
            throw ex;
        }
        /**
         * 动态代理类$Proxy0调用method方法时会调用它自己的相对应的method方法， 
         * 而它自己的方法里面调用的是super.h.invoke(this, , )，也就是父类Proxy的h的invoke方法， 
         * 也就是ReportCreatorProxy类的invoke方法。 
         * 所以，invoke(Object proxy, Method m, Object[] args)中的proxy实际上就是动态代理类$Proxy0， 
         * 如果你将其强转成XXXXXInterface然后调用它的方法，然后它就会调用super.h.invoke(this, , )，这样就会死循环。 
         */
        return result;
    }

    //主动切换代理
    public void changeClient(Object obj) {
        @SuppressWarnings("unchecked")
        HAProxy<T> regEvent = (HAProxy<T>) halProxy;
        regEvent.notifyHAListener(new HAEvent(obj));
    }


    /**
     * 静态工厂方法，用于获取动态代理实例
     *
     * @param target
     * @return
     */
    public static Object getProxyInstance(Object target, Object halProxy, AccessPolicy strategy) {
        Class<?> targetClass = target.getClass();  
        /*
         * loader:  要代理类的类加载器
    	 * interfaces:  要代理类所实现的所有的接口
    	 * handler: 转发方法调用的调用处理类实例，即代理类 
    	 */
        ClassLoader loader = targetClass.getClassLoader();
        Class<?>[] interfaces = targetClass.getInterfaces();
        ProxyHandler<Object> handler = new ProxyHandler<Object>(target, halProxy, strategy);
        if (handler.proxyInstance == null) {
            // 创建并返回动态代理类实例
            handler.proxyInstance = Proxy.newProxyInstance(loader, interfaces, handler);
        }
        return handler.proxyInstance;
    }


}
