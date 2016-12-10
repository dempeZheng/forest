package com.zhizus.forest;

import com.google.common.collect.Maps;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.common.exception.ForestFrameworkException;
import io.netty.channel.Channel;

import java.util.Map;

/**
 * Created by Dempe on 2016/12/7.
 */
public class ForestContext {

    private final static ThreadLocal<ForestContext> contextMap = new ThreadLocal<ForestContext>();

    private Channel channel;

    private Message message;

    private Map<String, String> attrs = Maps.newHashMap();

    private ForestContext(Channel channel, Message message) {
        this.channel = channel;
        this.message = message;
    }

    public static Message getMessage() {
        return getForestContext().message;
    }

    public static Channel getChannel() {
        return getForestContext().channel;
    }

    public static String getAttr(String key) {
        return getForestContext().attrs.get(key);
    }

    public static String putAttr(String key, String value) {
        return getForestContext().attrs.put(key, value);
    }

    public static ForestContext getForestContext() {
        ForestContext forestContext = contextMap.get();
        if (forestContext == null) {
            throw new ForestFrameworkException("local thread context is null");
        }
        return forestContext;
    }

    public static void removeForestContext() {
        contextMap.remove();

    }

    public static void setForestContext(Channel channel, Message message) {
        contextMap.set(new ForestContext(channel, message));
    }

}
