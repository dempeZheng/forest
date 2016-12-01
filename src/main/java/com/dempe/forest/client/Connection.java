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

package com.dempe.forest.client;

import com.dempe.forest.codec.Message;
import com.dempe.forest.codec.Response;
import com.dempe.forest.core.exception.ForestFrameworkException;
import com.dempe.forest.transport.NettyResponseFuture;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelFuture;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public class Connection {
    /**
     * max request default count
     */
    private ChannelFuture future;
    private AtomicBoolean isConnected = new AtomicBoolean();

    public final static Map<Long, NettyResponseFuture<Response>> callbackMap = Maps.newConcurrentMap();

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


    public NettyResponseFuture<Response> write(Message message, long timeOut) {
        if (!isConnected()) {
            throw new ForestFrameworkException("client is not connected");
        }
        NettyResponseFuture responseFuture = new NettyResponseFuture(System.currentTimeMillis(), timeOut, message, future.channel(), new Promise<Response>());
        future.channel().writeAndFlush(message);
        registerCallbackMap(message.getHeader().getMessageID(), responseFuture);
        return responseFuture;
    }

    public void callback(Message message, long timeOut, Promise<Response> promise) {
        if (!isConnected()) {
            throw new ForestFrameworkException("client is not connected");
        }
        future.channel().writeAndFlush(message);
        NettyResponseFuture responseFuture = new NettyResponseFuture(System.currentTimeMillis(), timeOut, message, future.channel(), promise);
        registerCallbackMap(message.getHeader().getMessageID(), responseFuture);

    }

    public NettyResponseFuture registerCallbackMap(Long messageId, NettyResponseFuture<Response> responseFuture) {
        return callbackMap.put(messageId, responseFuture);
    }

    public NettyResponseFuture removeCallbackMap(Long messageId) {
        return callbackMap.remove(messageId);
    }
}
