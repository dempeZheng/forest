package com.dempe.ketty.name.client;

import com.dempe.ketty.client.ClientSender;
import com.dempe.ketty.common.utils.JSONBody;
import com.dempe.ketty.protocol.KettyRequest;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/12/10
 * Time: 16:57
 * To change this template use File | Settings | File Templates.
 */
public class NameClient extends ClientSender {

    private final static String REGISTER_URI = "/nameAction/registerServerByName";
    private final static String DE_REGISTER_URI = "/nameAction/deRegisterServerByName";

    public NameClient(String host, int port) {
        super(host, port);
    }

    public String registerServer(String name, String host, int port) {
        KettyRequest request = new KettyRequest(REGISTER_URI);
        request.setBody(new JSONBody()
                .set("name", name)
                .set("host", host)
                .set("port", port));
        return sendAndWait(request);
    }

    public String deRegistering(String name, String host, int port) {
        KettyRequest request = new KettyRequest(DE_REGISTER_URI);
        request.setBody(new JSONBody()
                .set("name", name)
                .set("host", host)
                .set("port", port));
        return sendAndWait(request);

    }
}
