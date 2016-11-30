package com.dempe.forest.core.invoker;

import com.dempe.forest.codec.Message;
import com.dempe.forest.codec.compress.Compress;
import com.dempe.forest.codec.serialize.Serialization;
import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.SerializeType;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/25
 * Time: 17:09
 * To change this template use File | Settings | File Templates.
 */
public class InvokerWrapper {

    private ActionMethod actionMethod;

    private Message message;

    public InvokerWrapper(ActionMethod actionMethod, Message message) {
        this.actionMethod = actionMethod;
        this.message = message;
    }

    public Object invoke() throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, IOException {
        byte[] payload = message.getPayload();
        Byte extend = message.getHeader().getExtend();
        Serialization serialization = SerializeType.getSerializationByExtend(extend);
        Compress compress = CompressType.getCompressTypeByValueByExtend(extend);
        payload = compress.unCompress(payload);
        Object[] args = serialization.deserialize(payload, Object[].class);
        return actionMethod.rateLimiterInvoker(args);
    }


    public Message getMessage() {
        return message;
    }


}
