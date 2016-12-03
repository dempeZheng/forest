package com.dempe.forest.client.proxy;

import com.dempe.forest.Constants;
import com.dempe.forest.ForestUtil;
import com.dempe.forest.client.ChannelPool;
import com.dempe.forest.codec.Header;
import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.ProtoVersion;
import com.dempe.forest.core.SerializeType;
import com.dempe.forest.transport.NettyClient;

import java.lang.reflect.Proxy;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/3 0003
 * Time: 下午 5:34
 * To change this template use File | Settings | File Templates.
 */
public class JdkProxy<T> {


    public <T> T getProxy(Class<T> clz, NettyClient client) {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{clz},
                new ReferInvocationHandler(new ChannelPool(client), clz));
    }
}


class ReferConfig {

    private SerializeType serializeType;

    private CompressType compressType;

    private int timeout;


    public SerializeType getSerializeType() {
        return serializeType;
    }

    public void setSerializeType(SerializeType serializeType) {
        this.serializeType = serializeType;
    }

    public CompressType getCompressType() {
        return compressType;
    }

    public void setCompressType(CompressType compressType) {
        this.compressType = compressType;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public static ReferConfig getDefaultInstance() {
        return new ReferConfig();
    }

    public static Header getDefHeader() {
        byte extend = ForestUtil.getExtend(SerializeType.fastjson, CompressType.compressNo);
        return new Header(Constants.MAGIC, ProtoVersion.VERSION_1.getVersion(), extend, "", 2000L);
    }
}