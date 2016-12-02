package com.dempe.forest.client.proxy;

import com.dempe.forest.Constants;
import com.dempe.forest.client.ChannelPool;
import com.dempe.forest.codec.Header;
import com.dempe.forest.codec.Message;
import com.dempe.forest.codec.Response;
import com.dempe.forest.codec.RpcProtocolVersion;
import com.dempe.forest.codec.compress.Compress;
import com.dempe.forest.codec.serialize.Serialization;
import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.ForestUtil;
import com.dempe.forest.core.MessageType;
import com.dempe.forest.core.SerializeType;
import com.dempe.forest.core.annotation.Action;
import com.dempe.forest.core.annotation.Export;
import com.dempe.forest.core.exception.ForestFrameworkException;
import com.dempe.forest.transport.NettyResponseFuture;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/1
 * Time: 9:47
 * To change this template use File | Settings | File Templates.
 */
public class ReferMethodInterceptor implements MethodInterceptor {

    private final static AtomicLong id = new AtomicLong(0);

    private ChannelPool channelPool;
    private Class clz;

//    CacheBuilder<String,>

    // TODO 容灾&负载均衡的支持
    public ReferMethodInterceptor(ChannelPool channelPool) {
        this.channelPool = channelPool;
    }

    public ReferMethodInterceptor(Class clz, ChannelPool channelPool) {
        this.channelPool = channelPool;
        this.clz = clz;
    }

    public long nextMessageId() {
        return id.incrementAndGet();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Export export = method.getAnnotation(Export.class);
        Action action = (Action) clz.getAnnotation(Action.class);
        if (export == null || action == null) {
            new ForestFrameworkException("method annotation Export or Action is null ");
        }
        String value = Strings.isNullOrEmpty(action.value()) ? method.getClass().getSimpleName() : action.value();
        String uri = Strings.isNullOrEmpty(export.uri()) ? method.getName() : export.uri();
        long timeOut = export.timeOut() <= 0 ? 5000 : export.timeOut();
        String headerURI = ForestUtil.buildURI(value, uri);
        byte extend = ForestUtil.getExtend(export.serializeType(), export.compressType(), MessageType.request);
        long messageID = nextMessageId();
        Header header = new Header(Constants.MAGIC,RpcProtocolVersion.VERSION_1.getVersion(),extend, messageID,headerURI);
        Message message = new Message();
        message.setHeader(header);
        Compress compress = CompressType.getCompressTypeByValueByExtend(extend);
        Serialization serialization = SerializeType.getSerializationByExtend(extend);
        byte[] serialize = serialization.serialize(objects);
        message.setPayload(compress.compress(serialize));
        NettyResponseFuture<Response> responseFuture = channelPool.write(message, timeOut);
        return responseFuture.getPromise().await().getResult();
    }

}
