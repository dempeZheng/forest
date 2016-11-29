package com.dempe.forest.example;

import com.dempe.forest.codec.serialize.FastJsonSerialization;
import com.dempe.forest.codec.serialize.Hessian2Serialization;
import com.dempe.forest.codec.serialize.Serialization;
import com.dempe.forest.core.invoker.InvokerWrapper;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/29
 * Time: 17:56
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        testFastJson();
    }

    public static void  testHession() throws IOException, ClassNotFoundException {
        InvokerWrapper invokerWrapper = new InvokerWrapper(null, null);
        Serialization serialization = new Hessian2Serialization();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput output = invokerWrapper.createOutput(outputStream);
        output.writeObject(serialization.serialize("test"));
        output.flush();
        byte[] body = outputStream.toByteArray();
        System.out.println(body.length);
        ObjectInput input = invokerWrapper.createInput(InvokerWrapper.getInputStream(body));
        String deserialize = serialization.deserialize((byte[]) input.readObject(), String.class);
        System.out.println(deserialize);
    }
    public static void  testFastJson() throws IOException, ClassNotFoundException {
        InvokerWrapper invokerWrapper = new InvokerWrapper(null, null);
        Serialization serialization = new FastJsonSerialization();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput output = invokerWrapper.createOutput(outputStream);
        output.writeObject(serialization.serialize("test"));
        output.flush();
        byte[] body = outputStream.toByteArray();
        System.out.println(body.length);
        ObjectInput input = invokerWrapper.createInput(InvokerWrapper.getInputStream(body));
        String deserialize = serialization.deserialize((byte[]) input.readObject(), String.class);
        System.out.println(deserialize);
    }
}
