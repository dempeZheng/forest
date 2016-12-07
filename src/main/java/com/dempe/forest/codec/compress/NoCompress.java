package com.dempe.forest.codec.compress;

import java.io.IOException;

/**
 * Created by Dempe on 2016/12/7.
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
