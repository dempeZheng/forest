package com.zhizus.forest.common;


import com.zhizus.forest.common.codec.serialize.FastJsonSerialization;
import com.zhizus.forest.common.codec.serialize.Hessian2Serialization;
import com.zhizus.forest.common.codec.serialize.KryoSerialization;
import com.zhizus.forest.common.codec.serialize.Serialization;

/**
 * Created by Dempe on 2016/12/7.
 */
public enum SerializeType {

    Kyro((byte) 0), Fastjson((byte) 1), Hession2((byte) 2);

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

    public final static SerializeType DEFAULT_SERIALIZE_TYPE = SerializeType.Kyro;
}
