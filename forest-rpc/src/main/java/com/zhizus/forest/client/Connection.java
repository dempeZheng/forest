/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zhizus.forest.client;

import com.google.common.collect.Maps;
import com.zhizus.forest.common.Constants;
import com.zhizus.forest.common.EventType;
import com.zhizus.forest.common.codec.Header;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.common.codec.Response;
import com.zhizus.forest.common.exception.ForestFrameworkException;
import com.zhizus.forest.transport.NettyResponseFuture;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;


public class

Connection implements Closeable {

    public final static Map<Long, NettyResponseFuture<Response>> callbackMap = Maps.newConcurrentMap();
    private final static Logger LOGGER = LoggerFactory.getLogger(Connection.class);
    private ChannelFuture future;
    private AtomicBoolean isConnected = new AtomicBoolean();
    private final static AtomicLong id = new AtomicLong(0);

    public Connection() {
        this.isConnected.set(false);
        this.future = null;
    }

    public ChannelFuture getFuture() {
        return future;
    }

    public void setFuture(ChannelFuture future) {
        this.future = future;
    }

    public boolean isConnected() {
        return isConnected.get();
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected.set(isConnected);
    }

    public NettyResponseFuture<Response> request(Message message, long timeOut) {
        if (!isConnected()) {
            throw new ForestFrameworkException("client is not connected");
        }
        message.getHeader().setMessageID(id.incrementAndGet());
        NettyResponseFuture responseFuture = new NettyResponseFuture(System.currentTimeMillis(), timeOut, message, future.channel(), new Promise());
        registerCallbackMap(message.getHeader().getMessageID(), responseFuture);
        try {
            future.channel().writeAndFlush(message);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            removeCallbackMap(message.getHeader().getMessageID());
        }
        return responseFuture;
    }

    public boolean ping() {
        Header heartBeatHeader = Header.HeaderMaker.newMaker().make();
        heartBeatHeader.setExtend(EventType.HEARTBEAT.getValue());
        Message message = new Message(heartBeatHeader, null);
        NettyResponseFuture<Response> request = request(message, Constants.DEFAULT_TIMEOUT);
        try {
            return request.getPromise().await().getCode() == Constants.DEF_PING_CODE;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    public NettyResponseFuture registerCallbackMap(Long messageId, NettyResponseFuture<Response> responseFuture) {
        return callbackMap.put(messageId, responseFuture);
    }

    public NettyResponseFuture removeCallbackMap(Long messageId) {
        return callbackMap.remove(messageId);
    }

    @Override
    public void close() throws IOException {
        future.channel().close();
    }
}
