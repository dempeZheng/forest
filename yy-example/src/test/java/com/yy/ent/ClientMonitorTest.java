package com.yy.ent;

import com.yy.ent.client.ClientSender;
import org.junit.Before;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 10:52
 * To change this template use File | Settings | File Templates.
 */
public abstract class ClientMonitorTest {


    public ClientSender clientSender = new ClientSender();

    @Before
    public void setUp() {
        clientSender.connect("localhost", 8888);
        init();
    }

    public abstract void init();


}
