package com.dempe.forest.client.proxy;

import com.dempe.forest.Constants;
import com.dempe.forest.ForestUtil;
import com.dempe.forest.client.ChannelPool;
import com.dempe.forest.codec.Header;
import com.dempe.forest.codec.Message;
import com.dempe.forest.codec.Response;
import com.dempe.forest.codec.compress.Compress;
import com.dempe.forest.codec.serialize.Serialization;
import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.ProtoVersion;
import com.dempe.forest.core.SerializeType;
import com.dempe.forest.core.annotation.MethodProvider;
import com.dempe.forest.core.annotation.ServiceProvider;
import com.dempe.forest.core.exception.ForestFrameworkException;
import com.dempe.forest.transport.NettyResponseFuture;
import com.google.common.base.Strings;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/3 0003
 * Time: 下午 5:36
 * To change this template use File | Settings | File Templates.
 */
public class ReferInvocationHandler implements InvocationHandler {

    private final static AtomicLong id = new AtomicLong(0);

    private ChannelPool pool;

    private Class<?> target;

    public ReferInvocationHandler(ChannelPool pool, Class<?> target) {
        this.pool = pool;
        this.target = target;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ServiceProvider serviceProvider = target.getAnnotation(ServiceProvider.class);
        MethodProvider methodProvider = method.getAnnotation(MethodProvider.class);
        if (methodProvider == null || serviceProvider == null) {
            new ForestFrameworkException("method annotation Export or Action is null ");
        }
        String value = Strings.isNullOrEmpty(serviceProvider.serviceName()) ? method.getClass().getSimpleName() : serviceProvider.serviceName();
        String uri = Strings.isNullOrEmpty(methodProvider.methodName()) ? method.getName() : methodProvider.methodName();
        long timeOut = methodProvider.timeout() <= 0 ? 5000 : methodProvider.timeout();
        String headerURI = ForestUtil.buildUri(value, uri);
        byte extend = ForestUtil.getExtend(methodProvider.serializeType(), methodProvider.compressType());
        Header header =  new Header(Constants.MAGIC, ProtoVersion.VERSION_1.getVersion(), extend, headerURI, timeOut);
        header.setMessageID(id.incrementAndGet());
        Message message = new Message();
        message.setHeader(header);
        Compress compress = CompressType.getCompressTypeByValueByExtend(header.getExtend());
        Serialization serialization = SerializeType.getSerializationByExtend(header.getExtend());
        byte[] serialize = serialization.serialize(args);
        message.setPayload(compress.compress(serialize));
        NettyResponseFuture<Response> responseFuture = pool.write(message, header.getTimeOut());
        return responseFuture.getPromise().await().getResult();
    }


}


