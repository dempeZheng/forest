package com.yy.ent.client.pool;

import com.yy.ent.client.ClientSender;
import com.yy.ent.client.YYClient;
import org.apache.commons.pool.PoolableObjectFactory;

/**
 * Created with IntelliJ IDEA.
 * User: zhengdaxia
 * Date: 15/10/24
 * Time: 上午11:44
 * To change this template use File | Settings | File Templates.
 */
public class PoolableClientFactory implements PoolableObjectFactory<ClientSender> {

    private String host;

    private int port;

    public PoolableClientFactory(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public ClientSender makeObject() throws Exception {
        return new ClientSender(host,port);
    }

    @Override
    public void destroyObject(ClientSender obj) throws Exception {
        obj.close();
    }

    @Override
    public boolean validateObject(ClientSender obj) {
        if(obj instanceof YYClient){
            return true;
        }
        return false;
    }

    @Override
    public void activateObject(ClientSender obj) throws Exception {

    }

    @Override
    public void passivateObject(ClientSender obj) throws Exception {


    }
}
