package com.dempe.forest.conf;

import com.dempe.forest.codec.Codec;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/25
 * Time: 18:16
 * To change this template use File | Settings | File Templates.
 */
public class ServerConf {
    protected Codec<?> codec; //编解码器
    protected int threads = Math.max(24, Runtime.getRuntime().availableProcessors() * 2); //工作线程数
    protected int timeout = 10 * 1000; //请求超时时间
    protected int accepts = 65535; //连接数限制(目前仅用于服务端)
    protected int connectTimeout = 3 * 1000; //连接超时时间

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getAccepts() {
        return accepts;
    }

    public void setAccepts(int accepts) {
        this.accepts = accepts;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Codec<?> getCodec() {
        return codec;
    }

    public void setCodec(Codec<?> codec) {
        this.codec = codec;
    }


}
