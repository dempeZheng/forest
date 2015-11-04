package com.dempe.ketty;

import com.dempe.ketty.client.ClientSender;
import com.dempe.ketty.client.pool.ClientPool;
import org.junit.Before;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 10:52
 * To change this template use File | Settings | File Templates.
 */
public abstract class ClientMonitorTest {


    public ClientSender clientSender = new ClientSender("localhost", 8888);

    public ClientPool pool = new ClientPool("localhost", 8888);

    @Before
    public void setUp() {

        init();
    }

    public abstract void init();


}
