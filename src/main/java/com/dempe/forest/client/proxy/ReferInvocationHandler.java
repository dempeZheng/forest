package com.dempe.forest.client.proxy;


import com.dempe.forest.Constants;
import com.dempe.forest.codec.Header;
import com.dempe.forest.codec.RpcProtocolVersion;
import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.ForestUtil;
import com.dempe.forest.core.MessageType;
import com.dempe.forest.core.annotation.Action;
import com.dempe.forest.core.annotation.Export;
import com.dempe.forest.core.exception.ForestFrameworkException;
import com.google.common.base.Strings;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/30
 * Time: 15:36
 * To change this template use File | Settings | File Templates.
 */
public class ReferInvocationHandler implements InvocationHandler {

    private final static AtomicLong id = new AtomicLong(0);

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        Export export = method.getAnnotation(Export.class);
        Action action = method.getClass().getAnnotation(Action.class);
        if (export != null || action != null) {
            new ForestFrameworkException("method annotation Export or Action is null ");
        }
        String value = Strings.isNullOrEmpty(action.value()) ? method.getClass().getSimpleName() : action.value();
        String uri = Strings.isNullOrEmpty(export.uri()) ? method.getName() : export.uri();
        String headerURI = ForestUtil.buildURI(value, uri);
        byte extend = ForestUtil.getExtend(export.serializeType(), export.compressType(), MessageType.request);
        Header header = new Header();
        header.setMagic(Constants.MAGIC);
        header.setVersion(RpcProtocolVersion.VERSION_1.getVersion());
        header.setMessageID(nextMessageId());
        header.setUri(headerURI);
        header.setExtend(extend);



        return null;
    }

    public long nextMessageId() {
        return id.incrementAndGet();
    }
}
