package com.dempe.ketty.name.client;

import com.dempe.ketty.ha.HAProxy;
import com.dempe.ketty.ha.ServerInfo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/12/10
 * Time: 17:07
 * To change this template use File | Settings | File Templates.
 */
public class HANameClient extends HAProxy<NameClient>{

    @Override
    protected List<ServerInfo> initServerInfo(String conf) throws Exception {
        return null;
    }

    @Override
    protected NameClient createClient(ServerInfo serverInfo) throws Exception {
        return null;
    }
}
