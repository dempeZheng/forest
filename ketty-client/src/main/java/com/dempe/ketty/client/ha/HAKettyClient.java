package com.dempe.ketty.client.ha;

import com.dempe.ketty.client.Client;
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
public class HAKettyClient extends HAProxy<Client> {

    private String url;

    private int accessPolicy = 20000;

    public HAKettyClient(String url) {
        this.url = url;
    }


    public HAKettyClient(String url, int accessPolicy) throws Exception {
        this.url = url;
        this.accessPolicy = accessPolicy;
        initServer(url);
    }

    @Override
    protected List<ServerInfo> initServerInfo(String url) throws Exception {
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
    protected Client createClient(ServerInfo serverInfo) throws Exception {
        AccessPolicy policy = new AccessPolicy(this.accessPolicy);
        Client client = (Client) ProxyHandler.getProxyInstance(new KettyClient(serverInfo), this, policy);
        return client;
    }
}
