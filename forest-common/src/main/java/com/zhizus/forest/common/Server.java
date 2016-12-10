package com.zhizus.forest.common;

/**
 * Created by Dempe on 2016/12/8.
 */
public class Server {

    public static interface MetaInfo {

        String getAppName();

        String getServerGroup();

        String getServiceIdForDiscovery();

        String getInstanceId();

    }

    private String host;
    private int port;
    private volatile String id;


}
