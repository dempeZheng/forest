package com.zhizus.forest.common;

import org.apache.curator.x.discovery.ServiceInstance;

/**
 * Created by Dempe on 2016/12/20.
 */
public class ServerInfo<T> {

    private String host;

    private int port;

    private T client;

    public ServerInfo(ServiceInstance instance) {
        this.host = instance.getAddress();
        this.port = instance.getPort();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public T getClient() {
        return client;
    }

    public void setClient(T client) {
        this.client = client;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((host == null) ? 0 : host.hashCode());
        result = prime * result + port;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ServerInfo other = (ServerInfo) obj;
        if (host == null) {
            if (other.host != null)
                return false;
        } else if (!host.equals(other.host))
            return false;
        if (port != other.port)
            return false;
        return true;
    }
}
