package com.dempe.forest.core.invoker;

import com.dempe.forest.codec.Message;
import com.dempe.forest.codec.serialize.Hessian2Serialization;
import com.dempe.forest.codec.serialize.Serialization;
import com.dempe.forest.core.exception.ForestErrorMsgConstant;
import com.dempe.forest.core.exception.ForestFrameworkException;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.zip.GZIPInputStream;

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
        Serialization serialization = new Hessian2Serialization();

        Class<?>[] parameterTypes = actionMethod.getMethod().getParameterTypes();
        Object[] args = new Object[parameterTypes.length];
        ObjectInput input = createInput(getInputStream(payload));
        for (int i = 0; i < args.length; i++) {
            Object param = serialization.deserialize((byte[]) input.readObject(), parameterTypes[i]);
            args[i] = param;
        }
        return actionMethod.rateLimiterInvoker(args);
    }


    public Message getMessage() {
        return message;
    }

    /**
     * 获取输入流。兼容gzip
     *
     * @param data
     * @return
     */
    public static InputStream getInputStream(byte[] data) {
        InputStream ret = new ByteArrayInputStream(data);
//        try {
//            GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(data));
//            return gis;
//        } catch (Exception ignore) {}
        return ret;
    }

    public ObjectInput createInput(InputStream in) {
        try {
            return new ObjectInputStream(in);
        } catch (Exception e) {
            throw new ForestFrameworkException(this.getClass().getSimpleName() + " createInput error", e,
                    ForestErrorMsgConstant.FRAMEWORK_DECODE_ERROR);
        }
    }


    public ObjectOutput createOutput(OutputStream outputStream) {
        try {
            return new ObjectOutputStream(outputStream);
        } catch (Exception e) {
            throw new ForestFrameworkException(this.getClass().getSimpleName() + " createOutput error", e,
                    ForestErrorMsgConstant.FRAMEWORK_ENCODE_ERROR);
        }
    }

}
