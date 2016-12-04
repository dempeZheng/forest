package com.dempe.forest.core;

import com.dempe.forest.codec.compress.Compress;
import com.dempe.forest.codec.compress.GZipCompress;
import com.dempe.forest.codec.compress.NoCompress;
import com.dempe.forest.codec.compress.SnappyCompress;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/30
 * Time: 11:58
 * To change this template use File | Settings | File Templates.
 */
public enum CompressType {

    compressNo((byte) 0), gizp((byte) (1 << 4)), snappy((byte) (1<< 5));

    private byte value;


    CompressType(byte value) {
        this.value = value;
    }

    public static Compress getCompressTypeByValueByExtend(byte extend) {
        switch (extend & 0x30) {
            case 0x0:
                return new NoCompress();
            case 1 << 4:
                return new GZipCompress();
            case 1 << 5:
                return new SnappyCompress();
            default:
                return new NoCompress();
        }
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

}
