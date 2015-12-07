package com.dempe.ketty.client.ha;

import com.dempe.ketty.client.KettyClient;
import com.dempe.ketty.common.access.AccessPolicy;
import com.dempe.ketty.ha.HAProxy;
import com.dempe.ketty.ha.ProxyHandler;
import com.dempe.ketty.ha.ServerInfo;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 高可用的ketty客户端
 * User: Dempe
 * Date: 2015/12/7
 * Time: 10:48
 * To change this template use File | Settings | File Templates.
 */
public class HAKettyClient extends HAProxy<KettyClient> {

    private String url;

    private int accessPolicy = 10;

    public HAKettyClient(String url) {
        this.url = url;
    }


    public HAKettyClient(String url, int accessPolicy) {
        this.url = url;
        this.accessPolicy = accessPolicy;
    }

    @Override
    protected List<ServerInfo> initServerInfo(String conf) throws Exception {
        List<ServerInfo> serverInfoList = new ArrayList<ServerInfo>();
        String[] arr = url.split(",");
        int index = 0;
        for (String str : arr) {
            if (StringUtils.isNotBlank(str)) {
                ServerInfo serverInfo = new ServerInfo();
                String host = StringUtils.substringBefore(str, ":");
                int port = Integer.parseInt(StringUtils.substringAfter(str, ":"));
                serverInfo.setIp(host);
                serverInfo.setPort(port);
                serverInfo.setIndex(index);
                serverInfoList.add(serverInfo);
                index++;

            }
        }
        return serverInfoList;
    }

    @Override
    protected KettyClient createClient(ServerInfo serverInfo) throws Exception {
        AccessPolicy policy = new AccessPolicy(this.accessPolicy);
        KettyClient kettyClient = (KettyClient) ProxyHandler.getProxyInstance(new KettyClient(serverInfo), this, policy);
        return kettyClient;
    }
}
