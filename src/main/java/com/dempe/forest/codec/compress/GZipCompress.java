package com.dempe.forest.codec.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/28
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
public class GZipCompress implements Compress {

    private static final int BUFFER_SIZE = 256;

    public byte[] compress(byte[] array) throws IOException {
        if (array == null) {
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(array);
            gzip.close();
        } catch (IOException e) {
            throw e;
        }
        return out.toByteArray();
    }


    public byte[] unCompress(byte[] array) throws IOException {
        if (array == null) {
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(array);

        try {
            GZIPInputStream gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[BUFFER_SIZE];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            throw e;
        }
        return out.toByteArray();
    }

}
