package com.zhizus.forest.common;


import com.zhizus.forest.common.codec.compress.Compress;
import com.zhizus.forest.common.codec.compress.GZipCompress;
import com.zhizus.forest.common.codec.compress.NoCompress;
import com.zhizus.forest.common.codec.compress.SnappyCompress;

/**
 * Created by Dempe on 2016/12/7.
 */
public enum CompressType {

    None((byte) 0), GZIP((byte) (1 << 4)), Snappy((byte) (1 << 5));

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

    public final static CompressType DEFAULT_COMPRESS_TYPE = CompressType.None;

}
