package com.dempe.forest.core.invoker;

import com.dempe.forest.codec.Message;
import com.dempe.forest.codec.serialize.Serialization;
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
        Serialization serialization = SerializeType.getSerializationByExtend(message.getHeader().getExtend());
        Object[] args = serialization.deserialize(payload, Object[].class);
        return actionMethod.rateLimiterInvoker(args);
    }


    public Message getMessage() {
        return message;
    }


}
