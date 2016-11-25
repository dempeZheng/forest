package com.dempe.forest.transport;

import com.dempe.forest.codec.Codec;
import com.dempe.forest.conf.ServerConf;
import com.dempe.forest.core.handler.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/25
 * Time: 18:14
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractServer.class);

    private InetSocketAddress bindAddress;
    private volatile boolean closed;
    protected Codec<?> codec;

    private volatile ServerConf serverConf;

    public AbstractServer(ServerConf serverConf) throws Exception {
        this.serverConf = serverConf;
        codec = serverConf.getCodec();
        try {
            doBind();
        } catch (Throwable t) {
            LOGGER.error(t.getMessage(), t);
            throw new Exception("Failed to bind " + getClass().getSimpleName() + " on " + getBindAddress() + ", cause: " + t.getMessage(),
                    t);
        }
    }

    protected abstract void doBind() throws Throwable;

    public InetSocketAddress getBindAddress() {
        return bindAddress;
    }

    public void close() {
        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    public Codec<?> getCodec() {
        return codec;
    }

    public void setCodec(Codec<?> codec) {
        this.codec = codec;
    }


    /* (non-Javadoc)
     * @see com.yy.ent.yyp.transport.Endpoint#getHandler()
     */
    public Handler getHandler() {
        return null;
    }

    public ServerConf getServerConf() {
        return null;
    }
}
