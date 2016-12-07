package com.zhizus.forest.common.codec.compress;


import org.xerial.snappy.Snappy;

import java.io.IOException;

/**
 * Created by Dempe on 2016/12/7.
 */
public class SnappyCompress implements Compress {

    public byte[] compress(byte[] array) throws IOException {
        if (array == null) {
            return null;
        }
        return Snappy.compress(array);
    }


    public byte[] unCompress(byte[] array) throws IOException {
        if (array == null) {
            return null;
        }
        return Snappy.uncompress(array);
    }
}
