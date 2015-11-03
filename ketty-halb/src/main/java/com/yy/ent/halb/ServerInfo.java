package com.yy.ent.halb;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/23
 * Time: 20:28
 * To change this template use File | Settings | File Templates.
 */
public class ServerInfo<T> {
    /**
     * 位置
     */
    @Deprecated
    private int index;

    /**
     * 权重
     */
    private int weight;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 端口
     */
    private int port;

    /**
     * 命中
     */
    private long hits;

    private T client;

    /**
     * 是否是缺省的
     * true:缺省
     * false:非缺省
     */
    private boolean isDefault;

    public ServerInfo() {

    }

    /**
     * 构造方法
     *
     * @param ip   地址
     * @param port 端口
     */
    public ServerInfo(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * 构造方法(index 属性过期,调用不包含index的构造方法)
     *
     * @param index 位置
     * @param ip    地址
     * @param port  端口
     */
    @Deprecated
    public ServerInfo(int index, String ip, int port) {
        this.index = index;
        this.ip = ip;
        this.port = port;
    }

    /**
     * 构造方法
     *
     * @param ip     地址
     * @param port   端口
     * @param weight 权重
     */
    public ServerInfo(String ip, int port, int weight) {
        this(ip, port);
        this.weight = weight;
    }

    /**
     * 构造方法(index 属性过期,调用不包含index的构造方法)
     *
     * @param index  位置
     * @param ip     地址
     * @param port   端口
     * @param weight 权重
     */
    @Deprecated
    public ServerInfo(int index, String ip, int port, int weight) {
        this(index, ip, port);
        this.weight = weight;
    }

    /**
     * 构造方法(index 属性过期,调用不包含index的构造方法)
     *
     * @param index     位置
     * @param ip        地址
     * @param port      端口
     * @param weight    权重
     * @param isDefault 1:缺省   0:非缺省
     */
    @Deprecated
    public ServerInfo(int index, String ip, int port, int weight, int isDefault) {
        this(index, ip, port);
        this.weight = weight;
        this.isDefault = isDefault == 1 ? true : false;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public long getHits() {
        return hits;
    }

    public void setHits(long hits) {
        this.hits = hits;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public T getClient() {
        return client;
    }

    public void setClient(T client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "index:" + this.getIndex()
                + " ip:" + this.getIp() + " port:" + this.getPort()
                + " weight:" + this.getWeight() + " hits:" + this.getHits() + " isDefault:" + this.isDefault();
    }


}
