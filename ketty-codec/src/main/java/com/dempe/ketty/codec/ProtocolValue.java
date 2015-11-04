package com.dempe.ketty.codec;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/15
 * Time: 18:13
 * To change this template use File | Settings | File Templates.
 */
public class ProtocolValue {

    public static int combine(int len, int protoType) {
        return protoType << 24 | len;
    }

    public static int[] parse(int firstValue) {
        int nProtoType = firstValue >> 24;
        int packetSize = firstValue & 0x00ffffff;
        return new int[]{nProtoType, packetSize};
    }
}
