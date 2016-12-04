package com.dempe.forest.client.proxy;

import com.dempe.forest.ClientConfig;
import com.dempe.forest.Constants;
import com.dempe.forest.ForestUtil;
import com.dempe.forest.client.ChannelPool;
import com.dempe.forest.codec.Header;
import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.ProtoVersion;
import com.dempe.forest.core.SerializeType;
import com.dempe.forest.core.annotation.MethodProvider;
import com.dempe.forest.core.annotation.ServiceProvider;
import com.dempe.forest.core.exception.ForestFrameworkException;
import com.dempe.forest.transport.NettyClient;
import com.google.common.base.Strings;
import org.aeonbits.owner.ConfigFactory;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/3 0003
 * Time: 下午 5:34
 * To change this template use File | Settings | File Templates.
 */
public class ReferConfig {

    private SerializeType serializeType;

    private CompressType compressType;

    private int timeout;

    private String serviceName;

    private String methodName;

    private ChannelPool pool;

    public ReferConfig() {
        try {
            pool = new ChannelPool(new NettyClient(ConfigFactory.create(ClientConfig.class)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static ReferConfig makeReferConfig() {
        return new ReferConfig();
    }

    public static ReferConfig makeReferConfigByAnnotation(Class<?> target, Method method) {
        ServiceProvider serviceProvider = target.getAnnotation(ServiceProvider.class);
        MethodProvider methodProvider = method.getAnnotation(MethodProvider.class);
        if (methodProvider == null || serviceProvider == null) {
            new ForestFrameworkException("method annotation Export or Action is null ");
            return null;
        }
        String serviceName = Strings.isNullOrEmpty(serviceProvider.serviceName()) ? method.getClass().getSimpleName() : serviceProvider.serviceName();
        String methodName = Strings.isNullOrEmpty(methodProvider.methodName()) ? method.getName() : methodProvider.methodName();
        int timeout = methodProvider.timeout() <= 0 ? 5000 : methodProvider.timeout();
        return makeReferConfig()
                .setServiceName(serviceName)
                .setMethodName(methodName)
                .setTimeout(timeout)
                .setSerializeType(methodProvider.serializeType())
                .setCompressType(methodProvider.compressType());

    }

    public SerializeType getSerializeType() {
        return serializeType;
    }

    public ReferConfig setSerializeType(SerializeType serializeType) {
        this.serializeType = serializeType;
        return this;
    }

    public CompressType getCompressType() {
        return compressType;
    }

    public ReferConfig setCompressType(CompressType compressType) {
        this.compressType = compressType;
        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    public ReferConfig setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public String getServiceName() {
        return serviceName;
    }

    public ReferConfig setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public ReferConfig setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public Header makeHeader() {
        byte extend = ForestUtil.getExtend(serializeType, compressType);
        return new Header(Constants.MAGIC, ProtoVersion.VERSION_1.getVersion(), extend,
                ForestUtil.buildUri(serviceName, methodName), timeout);
    }


    public boolean isInit() {
        return Strings.isNullOrEmpty(serviceName)
                && Strings.isNullOrEmpty(methodName)
                && timeout == 0
                && serializeType == null
                && compressType == null;

    }

    public ChannelPool getPool() {
        return pool;
    }

    public ReferConfig setPool(ChannelPool pool) {
        this.pool = pool;
        return this;
    }


}
