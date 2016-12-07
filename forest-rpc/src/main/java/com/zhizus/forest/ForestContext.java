package com.zhizus.forest;

import com.zhizus.forest.common.codec.Header;
import com.zhizus.forest.common.exception.ForestFrameworkException;
import com.google.common.collect.Maps;
import io.netty.channel.Channel;

import java.util.Map;

/**
 * Created by Dempe on 2016/12/7.
 */
public class ForestContext {

    private final static ThreadLocal<ForestContext> contextMap = new ThreadLocal<ForestContext>();

    private Channel channel;

    private Header header;

    private Map<String, String> attrs = Maps.newHashMap();

    private ForestContext(Channel channel, Header header) {
        this.channel = channel;
        this.header = header;
    }

    public static Header getHeader() {
        return getForestContext().header;
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

    public static void setForestContext(Channel channel, Header header) {
        contextMap.set(new ForestContext(channel, header));
    }

}
