package com.zhizus.forest.common;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/25
 * Time: 16:38
 * To change this template use File | Settings | File Templates.
 */
public class Constants {

    public static final short MAGIC = (short) 0xF0F0;

    public static final int HEADER_SIZE = 16;

    public static final int HEADER_SIZE_NEW = 18;

    public static final byte RSP_TYPE = (byte) (1 << 7);


    public static final String DEF_GROUP = "def_group";

    public final static int DEFAULT_TIMEOUT = 5000;

    public static final String SERVICE_KEY_PREIFX = "forest-rpc:";

    public static final String PBRPC_SCHEME = "rpc";

    public final static int CONNECTION_TIMEOUT = 2000;

    public final static String BASE_PATH = "forest/service";

    public final static int DEF_PORT = 9999;
}
