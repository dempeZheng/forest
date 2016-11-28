package com.dempe.forest.codec.compress;


import org.xerial.snappy.Snappy;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/28
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
public class SnappyCompress {

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
