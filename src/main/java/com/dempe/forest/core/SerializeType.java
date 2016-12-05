package com.dempe.forest.core;

import com.dempe.forest.codec.serialize.FastJsonSerialization;
import com.dempe.forest.codec.serialize.Hessian2Serialization;
import com.dempe.forest.codec.serialize.KryoSerialization;
import com.dempe.forest.codec.serialize.Serialization;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/30
 * Time: 11:58
 * To change this template use File | Settings | File Templates.
 */
public enum SerializeType {

    kyro((byte) 0), fastjson((byte) 1), hession2((byte) 2);

    private byte value;

    SerializeType(byte value) {
        this.value = value;
    }

    public static Serialization getSerializationByExtend(byte value) {
        switch (value & 0x7) {
            case 0x0:
                return new KryoSerialization();
            case 0x1:
                return new FastJsonSerialization();
            case 0x2:
                return new Hessian2Serialization();
            default:
                return new KryoSerialization();
        }

    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public final static SerializeType DEFAULT_SERIALIZE_TYPE = SerializeType.kyro;
}
