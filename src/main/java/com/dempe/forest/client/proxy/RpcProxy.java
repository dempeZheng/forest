package com.dempe.forest.client.proxy;

import com.dempe.forest.MethodProviderConf;
import com.dempe.forest.RefConfMapping;
import com.dempe.forest.ReferConfig;
import com.dempe.forest.codec.Header;
import com.dempe.forest.codec.Message;
import com.dempe.forest.codec.Response;
import com.dempe.forest.codec.compress.Compress;
import com.dempe.forest.codec.serialize.Serialization;
import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.SerializeType;
import com.dempe.forest.core.annotation.MethodProvider;
import com.dempe.forest.core.annotation.ServiceProvider;
import com.dempe.forest.transport.NettyResponseFuture;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/4 0004
 * Time: 下午 2:30
 * To change this template use File | Settings | File Templates.
 */
public class RpcProxy implements InvocationHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(RpcProxy.class);

    private RefConfMapping refConfMapping;

    private List<ReferConfig> referConfigList = Lists.newArrayList();
    private String serviceName;

    public RpcProxy(RefConfMapping refConfMapping) {
        this.refConfMapping = refConfMapping;
    }

    public RpcProxy() {
        refConfMapping = new RefConfMapping();
    }

    private final static AtomicLong id = new AtomicLong(0);


    public <T> T getProxy(Class<T> clazz) {
        ServiceProvider serviceProvider = clazz.getAnnotation(ServiceProvider.class);
        if (serviceProvider == null) {
            LOGGER.warn("cannot getProxy for class:{}", clazz);
            return null;
        }
        String serviceName = Strings.isNullOrEmpty(serviceProvider.serviceName()) ? clazz.getSimpleName() : serviceProvider.serviceName();
        this.serviceName = serviceName;
        for (ReferConfig referConfig : referConfigList) {
            referConfig.setServiceName(serviceName);
            refConfMapping.registerRefConfMap(referConfig);
        }
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{clazz}, this);
    }

    public RefConfMapping getRefConfMapping() {
        return refConfMapping;
    }


    public RpcProxy setRefConfMapping(RefConfMapping refConfMapping) {
        this.refConfMapping = refConfMapping;
        return this;
    }

    public RpcProxy setMethodProviderConfig(String methodName, MethodProviderConf methodProviderConf) {
        ReferConfig referConfig = ReferConfig.makeReferConfig().setMethodName(methodName).setMethodProviderConf(methodProviderConf);
        this.referConfigList.add(referConfig);
        return this;


    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodProvider methodProvider = method.getAnnotation(MethodProvider.class);
        if (methodProvider == null) {
            LOGGER.info("method:{} cannot find methodProvider.", method.getName());
            return null;
        }
        String methodName = Strings.isNullOrEmpty(methodProvider.methodName()) ? method.getName() : methodProvider.methodName();
        ReferConfig refConf = refConfMapping.getRefConf(serviceName, methodName);
        if (refConf == null) {
            LOGGER.info("serviceName:{},methodName is not found", serviceName, methodName);
            return null;
        }
        Header header = refConf.makeHeader();
        header.setMessageID(id.incrementAndGet());
        Message message = new Message();
        message.setHeader(header);
        Compress compress = CompressType.getCompressTypeByValueByExtend(header.getExtend());
        Serialization serialization = SerializeType.getSerializationByExtend(header.getExtend());
        byte[] serialize = serialization.serialize(args);
        message.setPayload(compress.compress(serialize));
        NettyResponseFuture<Response> responseFuture = refConf.getPool().write(message, header.getTimeOut());
        return responseFuture.getPromise().await().getResult();
    }
}
