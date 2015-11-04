package com.dempe.ketty.client.pool;

import com.dempe.ketty.client.ClientSender;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/23
 * Time: 18:28
 * To change this template use File | Settings | File Templates.
 */
public class ClientPool extends GenericObjectPool<ClientSender> {


    public ClientPool(String host, int port) {
        super(new PoolableClientFactory(host, port));
    }

    public static void main(String[] args) throws Exception {


    }

}
