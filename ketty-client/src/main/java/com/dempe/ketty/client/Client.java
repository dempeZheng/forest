package com.dempe.ketty.client;

import com.dempe.ketty.protocol.KettyRequest;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/1/27
 * Time: 15:40
 * To change this template use File | Settings | File Templates.
 */
public interface Client {

    public void connect(final String host, final int port);

    public void close() throws IOException;

    public void send(KettyRequest request);

}
