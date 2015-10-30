package com.yy.ent.client;

import com.yy.ent.protocol.JettyResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/21
 * Time: 17:22
 * To change this template use File | Settings | File Templates.
 */
public class ReplyFuture {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);

    private long messageId;

    private long readTimeoutMillis = 120000;

    private JettyResp message;


    public ReplyFuture(long messageId) {
        this.messageId = messageId;
    }

    public long getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

    public void setReadTimeoutMillis(long readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public synchronized void await() {
        await(readTimeoutMillis);
    }

    public synchronized void await(long millis) {
        try {
            if (message == null) {
                this.wait(millis);
            }
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    synchronized void onReceivedReply(JettyResp message) {
        this.message = message;
        this.notifyAll();
    }

    public String getReply() {
        if (this.message == null) {
            await();
        }
        if (this.message == null) {
            LOGGER.error("message is null");
        }
        return this.message.getData();
    }
}
