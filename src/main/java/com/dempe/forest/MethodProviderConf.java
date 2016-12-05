package com.dempe.forest;

import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.SerializeType;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/5
 * Time: 9:53
 * To change this template use File | Settings | File Templates.
 */
public class MethodProviderConf {

    private SerializeType serializeType;

    private CompressType compressType;

    private int timeout;

    public SerializeType getSerializeType() {
        return serializeType;
    }

    public MethodProviderConf setSerializeType(SerializeType serializeType) {
        this.serializeType = serializeType;
        return this;
    }

    public CompressType getCompressType() {
        return compressType;
    }

    public MethodProviderConf setCompressType(CompressType compressType) {
        this.compressType = compressType;
        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    public MethodProviderConf setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public static MethodProviderConf makeMethodProviderConf() {
        return new MethodProviderConf();
    }
}
