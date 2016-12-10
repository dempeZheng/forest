package com.zhizus.forest.common.codec.compress;

import java.io.IOException;

/**
 * Created by Dempe on 2016/12/7.
 */
public interface Compress {

    byte[] compress(byte[] array) throws IOException;


    byte[] unCompress(byte[] array) throws IOException;
}
