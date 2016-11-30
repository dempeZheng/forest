package com.dempe.forest.codec.compress;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/30
 * Time: 13:14
 * To change this template use File | Settings | File Templates.
 */
public class NoCompress implements Compress {

    @Override
    public byte[] compress(byte[] array) throws IOException {
        return array;
    }

    @Override
    public byte[] unCompress(byte[] array) throws IOException {
        return array;
    }
}
