package com.dempe.forest.codec.compress;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/28
 * Time: 15:40
 * To change this template use File | Settings | File Templates.
 */
public interface Compress {

    byte[] compress(byte[] array) throws IOException;


    byte[] unCompress(byte[] array) throws IOException;
}
